package com.ayakovlev.main;

import com.opencsv.CSVWriter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonToCsvConverter {

    //URL target path
    private static final String targetURL = "http://api.goeuro.com/api/v2/position/suggest/en/";

    //CSV file header
    private static final String[] FILE_HEADER = new String[]{"_id", "name", "type", "latitude", "longitude"};

    //CSV File name
    private static final String OUTPUT_CSV_FILE_NAME = "output.csv";

    public static void main(String[] args) {

        try {

            //get city from args
            URL inputData;
            if (args.length != 0)
                inputData = new URL(targetURL + args[0]);
            else {
                System.out.print("You should pass city as a first argument");
                return;
            }

            HttpURLConnection httpConnection = getHttpURLConnection(inputData);

            JsonArray jsonArray = getJsonValues(httpConnection);
            httpConnection.disconnect();

            List<String[]> dataToCSVFile = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                String[] rowToFile = getRowToFile(jsonArray, i);
                dataToCSVFile.add(rowToFile);
            }

            writeDataToCSVFile(dataToCSVFile);

            System.out.print("File has been successfully created!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* get httpConnection */
    private static HttpURLConnection getHttpURLConnection(URL restServiceURL) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("Accept", "application/json");

        if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error code : "
                    + httpConnection.getResponseCode());
        }
        return httpConnection;
    }

    /* get json data from InputStream of httpConnection */
    private static JsonArray getJsonValues(HttpURLConnection httpConnection) throws IOException {
        JsonReader jsonReader = Json.createReader(httpConnection.getInputStream());
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();
        return jsonArray;
    }

    /* create one line for csv file */
    private static String[] getRowToFile(JsonArray jsonArray, int i) {
        JsonObject jsonObject = jsonArray.getJsonObject(i);

        String id = jsonObject.getJsonNumber("_id").toString();
        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");

        JsonObject innerJsonObject = jsonObject.getJsonObject("geo_position");
        String latitude = innerJsonObject.getJsonNumber("latitude").toString();
        String longitude = innerJsonObject.getJsonNumber("longitude").toString();

        return new String[]{id, name, type, latitude, longitude};
    }

    /* write everything into file */
    private static void writeDataToCSVFile(List<String[]> dataToCSVFile) throws IOException {
        String csv = OUTPUT_CSV_FILE_NAME;
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        //write without Quotes
        writer.writeNext(FILE_HEADER, false);
        for (String[] arrayToFile : dataToCSVFile)
            writer.writeNext(arrayToFile, false);
        writer.close();
    }
}