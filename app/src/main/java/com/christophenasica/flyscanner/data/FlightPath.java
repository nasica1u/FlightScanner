package com.christophenasica.flyscanner.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

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
    private List<String[]> path;

    public FlightPath(JsonObject jsonObject) {
        icao24 = jsonObject.get(ICAO24) instanceof JsonNull ? "" : jsonObject.get(ICAO24).getAsString();
        startTime = jsonObject.get(START_TIME) instanceof JsonNull ? -1 : jsonObject.get(START_TIME).getAsInt();
        endTime = jsonObject.get(END_TIME) instanceof JsonNull ? -1 : jsonObject.get(END_TIME).getAsInt();
        callsign = jsonObject.get(CALLSIGN) instanceof JsonNull ? "" : jsonObject.get(CALLSIGN).getAsString();
        JsonArray jsonArray = jsonObject.get(PATH) instanceof JsonNull ? null : jsonObject.get(PATH).getAsJsonArray();
        if (jsonArray != null) {
            path = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                if (jsonElement instanceof JsonArray) {
                    JsonArray nestedArray = (JsonArray) jsonElement;
                    String[] array = new String[6];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = nestedArray.get(i) instanceof JsonNull ? "" : nestedArray.get(i).getAsString();
                    }
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

    public List<String[]> getPath() {
        return path;
    }
}
