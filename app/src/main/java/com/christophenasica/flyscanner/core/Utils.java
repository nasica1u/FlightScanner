package com.christophenasica.flyscanner.core;

import android.util.Log;

import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;
import com.christophenasica.flyscanner.data.FlightPath;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static Airport getAirportByIcao(String icao) {
        List<Airport> airports = AirportManager.getInstance().getAirportList();
        for (Airport airport : airports) {
            if (airport.getIcao().contentEquals(icao))
                return airport;
        }
        return null;
    }

    public static SimpleDateFormat getDateHourFormat() {
        String format = "dd/MM/yy HH:mm";
        return new SimpleDateFormat(format, Locale.US);
    }

    public static SimpleDateFormat getHourMinuteFormat() {
        String format = "HH:mm";
        return new SimpleDateFormat(format, Locale.US);
    }

    public static SimpleDateFormat getStandardDateFormat() {
        String format = "dd/MM/yy";
        return new SimpleDateFormat(format, Locale.US);
    }

    public static SimpleDateFormat getCompactDateFormat() {
        String format = "dd/MM";
        return new SimpleDateFormat(format, Locale.US);
    }

    // Time has to be in ms (otherwise do *1000L)
    public static String timestampToDateHourString(long time) {
        return getDateHourFormat().format(new Date(time));
    }

    public static String timestampToHourMinute(long time) {
        return getHourMinuteFormat().format(new Date(time));
    }

    public static String dateToString(Date date) {
        return dateToString(date, false);
    }

    public static String dateToString(Date date, boolean compactFormat) {
        if (compactFormat)
            return getCompactDateFormat().format(date);
        return getStandardDateFormat().format(date);
    }

    public static String timestampToString(long time) {
        return timestampToString(time, false);
    }

    public static String timestampToString(long time, boolean compactFormat) {
        Date date = new Date(time);
        if (compactFormat)
            return dateToString(date, true);
        return dateToString(date);
    }

    public static String formatFlightDuration(long time) {
        if (time < 1)
            return "";

        time = time / 60;
        long hour = time / 60;
        long minute = time % 60;
        StringBuilder duration = new StringBuilder();
        if (hour > 0) {
            duration.append(hour).append("h");
            if (minute < 10) {
                return duration.append("0").append(minute).toString();
            }
            return duration.append(minute).toString();
        }
        else {
            if (minute < 10) {
                return duration.append("0").append(minute).append("min").toString();
            }
            return duration.append(minute).append("min").toString();
        }
    }
}
