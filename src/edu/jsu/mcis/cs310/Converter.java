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
            JsonArray jsonrHeaders = new JsonArray();
            JsonArray jsoncHeaders = new JsonArray();
            JsonArray jsonData = new JsonArray();
          
            LinkedHashMap<String, JsonArray> jsonRecord = new LinkedHashMap<>();

            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> csvList = reader.readAll();

            Iterator<String[]> iterator = csvList.iterator();

            String[] csvHeadings = iterator.next();
            jsoncHeaders.addAll(Arrays.asList(csvHeadings));

            while(iterator.hasNext()){
                JsonArray csvData = new JsonArray();
                String[] csvRecords = iterator.next();

                for (int i = 0; i < csvRecords.length; i++){
                    if (csvHeadings[i].equals("ProdNum")){
                        jsonrHeaders.add(csvRecords[i]);
                    }
                    else if (csvHeadings[i].equals("Season") || csvHeadings[i].equals("Episode")){
                        csvData.add(Integer.valueOf(csvRecords[i]));
                    }
                    else{
                        csvData.add(csvRecords[i]);
                    }
                }
                jsonData.add(csvData);
            }

            jsonRecord.put("ProdNums", jsonrHeaders);
            jsonRecord.put("ColHeadings", jsoncHeaders);
            jsonRecord.put("Data", jsonData);

            String jsonString = Jsoner.serialize(jsonRecord);

            return jsonString;
        }
        catch (IOException e) {        
           e.printStackTrace();
        }       
        return result.trim();         
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
          StringWriter writer = new StringWriter();
        try {                      
  JsonObject json = Jsoner.deserialize(jsonString, new JsonObject());

            JsonArray rHeaders = (JsonArray) json.get("ProdNums");
            JsonArray cHeaders = (JsonArray) json.get("ColHeadings");
            JsonArray data = (JsonArray) json.get("Data");

            
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");

            List<String> csvColHeadingsList = new ArrayList<>();
            for (int i = 0; i < cHeaders.size(); i++) {
                csvColHeadingsList.add(cHeaders.getString(i));
            }

            int colHeadingsSize = csvColHeadingsList.size();
            String[] csvColHeadings = csvColHeadingsList.toArray(new String[colHeadingsSize]);
            csvWriter.writeNext(csvColHeadings);


            cHeaders.remove("ProdNum");


            for (int i = 0; i < rHeaders.size(); i++) {
                List<String> csvData = (List<String>) data.get(i);
                List<String> csvRecordList = new ArrayList<>();
                csvRecordList.add(rHeaders.getString(i));


                for(int j = 0; j < csvData.size(); j++){

                    if (cHeaders.get(j).equals("Season")){

                        csvRecordList.add(String.valueOf(csvData.get(j)));
                    }
                    else if (cHeaders.get(j).equals("Episode")){
                        Integer num = Integer.valueOf(String.valueOf(csvData.get(j)));
                        String numString = String.format("%02d", num);
                        csvRecordList.add(numString);
                    }
                    else{
                        csvRecordList.add(csvData.get(j));
                    }
                }

                String[] csvRecord = csvRecordList.toArray(new String[colHeadingsSize]);
                csvWriter.writeNext(csvRecord);   
            }

            String csvString = writer.toString();

            return csvString.trim();  
        
        }
        catch (Exception e) {          
            e.printStackTrace();
        }       
        
        return result.trim();        
        
    }   
    
}
