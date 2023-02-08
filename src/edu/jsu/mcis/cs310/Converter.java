package edu.jsu.mcis.cs310;
import java.io.*;
import java.util.*;
import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringWriter;
import java.io.StringReader;

public class Converter {
   
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        StringBuilder json = new StringBuilder();
        try {
        
            // INSERT YOUR CODE 
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> csv = reader.readAll();
            Iterator<String[]> iterator = csv.iterator();
            
            JsonArray rHeaders = new JsonArray();
            JsonArray cHeaders = new JsonArray();
            JsonArray data = new JsonArray();
            String[] rows;
            cHeaders.addAll(Arrays.asList(iterator.next()));
             while (iterator.hasNext()){
                 JsonArray row = new JsonArray();
                 rows = iterator.next();
                 rHeaders.add(rows[0]);
                  for (int i = 1; i < rows.length; i++){
                    row.add(rows[i]);
                  }
                  data.add(row);
             }
               json.append("{\n    \"colHeaders\":").append(cHeaders.toString());
            json.append(",\n    \"rowHeaders\":").append(rHeaders.toString()).append(",\n");
            rows = data.toString().split("],");
            json.append("    \"data\":");
              for (int i = 0; i < rows.length; ++i){               
                String s = rows[i];  
                s = s.replace("\"","");    
                s = s.replace("]]","]");                   
                json.append(s);                                     
                if ((i % rows.length) != (rows.length - 1))
                    json.append("],\n            ");                
            }                      
            json.append("\n    ]\n}");
             }                 
        catch (IOException e) {        
           e.printStackTrace();
        }       
        return json.toString();         
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
          StringWriter writer = new StringWriter();
        try {                      
                 JsonParser parser = new JsonParser();
             
            JsonObject jobject = (JsonObject) parser.parse(jsonString);
            JsonArray col = (JsonArray) jobject.get("colHeaders");
            JsonArray row = (JsonArray) jobject.get("rowHeaders");
            JsonArray data = (JsonArray) jobject.get("data");
                                 
            String[] csvcol = new String[col.size()];
            String[] csvrow = new String[row.size()];
            String[] csvdata = new String[data.size()];
            String[] rowdata;
                     
            for (int i = 0; i < col.size(); i++) {
                csvcol[i] = col.get(i) + "";
            }          
            for (int i = 0; i < row.size(); i++) {               
                csvrow[i] = row.get(i) + "";
                csvdata[i] = data.get(i) + "";
            }
            CSVWrit csvWriter = new CSVWrit(writer,',','"',"\n");

            csvWriter.writeNext(csvcol);
            for (int i = 0; i < csvdata.length; i++) {                             
                csvdata[i] = csvdata[i].replace("[","");
                csvdata[i] = csvdata[i].replace("]","");               
                String[] elements = csvdata[i].split(",");               
                rowdata = new String[elements.length + 1];
				rowdata[0] = csvrow[i];                           
				for(int d = 1; d < csvrow.length; d++){
					rowdata[d] = csvrow[d];
				}               
                csvWriter.writeNext(rowdata);               
            }
        }
        catch (Exception e) {          
            e.printStackTrace();
        }       
        
        return result.trim();        
        
    }   
    
}
