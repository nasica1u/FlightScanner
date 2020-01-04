package com.christophenasica.flyscanner.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class FlightPath {

    private static final String ICAO24 = "icao24";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String CALLSIGN = "callsign";
    private static final String PATH = "path";

    private String icao24;
    private int startTime;
    private int endTime;
    private String callsign;
    private ArrayList<String[]> path;

    public FlightPath(JsonObject jsonObject) {
        icao24 = jsonObject.get(ICAO24) instanceof JsonNull ? "" : jsonObject.get(ICAO24).getAsString();
        startTime = jsonObject.get(START_TIME) instanceof JsonNull ? 0 : jsonObject.get(START_TIME).getAsInt();
        endTime = jsonObject.get(END_TIME) instanceof JsonNull ? 0 : jsonObject.get(END_TIME).getAsInt();
        callsign = jsonObject.get(CALLSIGN) instanceof JsonNull ? "" : jsonObject.get(CALLSIGN).getAsString();
        JsonArray jsonArray = jsonObject.get(PATH) instanceof JsonNull ? null : jsonObject.get(PATH).getAsJsonArray();
        if (jsonArray != null) {
            path = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                if (jsonElement instanceof JsonArray) {
                    JsonArray nestedArray = (JsonArray) jsonElement;
                    String[] array = new String[6];
                    array[0] = nestedArray.get(0) instanceof JsonNull ? "" : nestedArray.get(0).getAsString();
                    array[1] = nestedArray.get(1) instanceof JsonNull ? "" : nestedArray.get(1).getAsString();
                    array[2] = nestedArray.get(2) instanceof JsonNull ? "" : nestedArray.get(2).getAsString();
                    array[3] = nestedArray.get(3) instanceof JsonNull ? "" : nestedArray.get(3).getAsString();
                    array[4] = nestedArray.get(4) instanceof JsonNull ? "" : nestedArray.get(4).getAsString();
                    array[5] = nestedArray.get(5) instanceof JsonNull ? "" : nestedArray.get(5).getAsString();
                    path.add(array);
                }
            }
        }
    }

    public String getIcao24() {
        return icao24;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getCallsign() {
        return callsign;
    }

    public ArrayList<String[]> getPath() {
        return path;
    }
}
