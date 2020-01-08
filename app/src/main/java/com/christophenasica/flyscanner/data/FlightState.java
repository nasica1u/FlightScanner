package com.christophenasica.flyscanner.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class FlightState {

    private static final String TAG = FlightState.class.getSimpleName();

    private static final String TIME = "time";
    private static final String STATES = "states";

    private int time;
    private List<String[]> states;

    public static final int ICAO24 = 0; // 0
    public static final int CALLSIGN = 1; // 0
    public static final int ORIGIN_COUNTRY = 2;
    public static final int TIME_POSITION = 3;
    public static final int LAST_CONTACT = 4;
    public static final int LONGITUDE = 5;
    public static final int LATITUDE = 6;
    public static final int ALTITUDE = 7;
    public static final int ON_GROUND = 8;
    public static final int VELOCITY = 9;
    public static final int TRUE_TRACK = 10;
    public static final int VERTICAL_RATE = 11;
    public static final int SENSORS = 12;
    public static final int GEO_ALTITUDE = 13;
    public static final int SQUAWK = 14;
    public static final int SPI = 15;
    public static final int POSITION_SOURCE = 16;

    public FlightState(JsonObject jsonObject) {
        time = jsonObject.get(TIME) instanceof JsonNull ? -1 : jsonObject.get(TIME).getAsInt();
        JsonArray jsonArray = jsonObject.get(STATES) instanceof JsonNull ? null : jsonObject.get(STATES).getAsJsonArray();
        if (jsonArray != null) {
            states = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                if (jsonElement instanceof JsonArray) {
                    JsonArray nestedArray = (JsonArray) jsonElement;
                    String[] array = new String[17];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = nestedArray.get(i) instanceof JsonNull ? "" : nestedArray.get(i).getAsString();
                    }
                    states.add(array);
                }
            }
        }
    }

    public int getTime() {
        return time;
    }

    public List<String[]> getStates() {
        return states;
    }
}
