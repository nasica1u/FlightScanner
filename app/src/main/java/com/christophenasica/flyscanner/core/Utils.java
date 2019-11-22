package com.christophenasica.flyscanner.core;

import android.util.Log;

import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static List<Airport> generateAirportList() {
        ArrayList<Airport> airports = new ArrayList<>();

        if (getAirportsListJson() != null) {
            for (JsonElement airportObject : getAirportsListJson()) {
                airports.add(new Gson().fromJson(airportObject.getAsJsonObject(), Airport.class));
            }
        }
        return airports;
    }

    public static JsonArray getAirportsListJson() {
        InputStream inputStream;
        try {
            inputStream = ApplicationManager.getAppAssets().open("airports.json");
            return JsonParser.parseString(getTextFromStream(inputStream)).getAsJsonArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getTextFromFile(File file) {
        String content = null;
        if (file.exists()) {
            try {
                InputStream inputStream = new FileInputStream(file);
                content = getTextFromStream(inputStream);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Error while opening to open text file: "+file.getName());
            }
        }
        else {
            Log.e(TAG, "Text file does not exist : "+file.getName());
        }
        return content;
    }

    private static String getTextFromStream(InputStream inputStream) {
        String text = null;
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtils.copy(inputStream, stringWriter, Charset.forName("UTF-8"));
            text = stringWriter.toString();
        } catch (IOException e) {
            Log.e(TAG, "Can't read text from stream", e);
        }
        return text;
    }

    public static boolean isStringValid(String str) {
        return str != null && !str.isEmpty();
    }

    public static List<Flight> convertFlightsJsonArrayToList(JsonArray jsonArray) {
        List<Flight> flightList = new ArrayList<>();
        for (JsonElement element: jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            Flight f = new Flight(jsonObject);
            flightList.add(f);
        }
        return flightList;
    }
}
