package com.christophenasica.flyscanner.data;

public class Airport {
    private String code;
    private String lat;
    private String lon;
    private String name;
    private String city;
    private String state;
    private String country;
    private String woeid;
    private String tz;
    private String phone;
    private String type;
    private String email;
    private String url;
    private String runway_length;
    private String elev;
    private String icao;
    private String direct_flights;
    private String carriers;

    public String getFormattedName() {
        return code + " - " + city + " " + "(" + country + ")";
    }
}