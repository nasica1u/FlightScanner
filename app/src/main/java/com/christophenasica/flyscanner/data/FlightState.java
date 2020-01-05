package com.christophenasica.flyscanner.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class FlightState {

    private static final String TIME = "time";
    private static final String STATES = "states";

    private int time;
    private List<String[]> states;

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
