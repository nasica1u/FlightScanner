package com.christophenasica.flyscanner.core;

import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AirportManager {

    private List<Airport> mAirportList;

    private static AirportManager airportManager;

    private AirportManager() {
        mAirportList = Utils.generateAirportList();
    }

    public static AirportManager getInstance() {
        if (airportManager == null)
            airportManager = new AirportManager();
        return airportManager;
    }

    public List<Airport> getAirportList() {
        return mAirportList;
    }

    public List<String> getAirportNameList() {
        ArrayList<String> airportNames = new ArrayList<>();
        for (Airport airport: getAirportList()) {
            airportNames.add(airport.getFormattedName());
        }
        return airportNames;
    }
}
