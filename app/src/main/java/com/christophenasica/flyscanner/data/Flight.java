package com.christophenasica.flyscanner.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable {

    public static final String DEPARTURE_AIRPORT = "estDepartureAirport";
    public static final String ARRIVAL_AIRPORT = "estArrivalAirport";
    public static final String TIME_DEPARTURE = "firstSeen";
    public static final String TIME_ARRIVAL = "lastSeen";
    public static final String ICAO24 = "icao24";

    private String airportDep;
    private String airportArr;
    private int dateDep;
    private int dateArr;
    private int flightTime;
    private String flightName;

    public Flight(String dep, String arr, int dDep, int dArr, String fName) {
        airportDep = dep;
        airportArr = arr;
        dateDep = dDep;
        dateArr = dArr;
        flightTime = dArr - dDep;
        flightName = fName;
    }

    protected Flight(Parcel in) {
        airportDep = in.readString();
        airportArr = in.readString();
        dateDep = in.readInt();
        dateArr = in.readInt();
        flightTime = in.readInt();
        flightName = in.readString();
    }

    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airportDep);
        dest.writeString(airportArr);
        dest.writeInt(dateDep);
        dest.writeInt(dateArr);
        dest.writeInt(flightTime);
        dest.writeString(flightName);
    }

    public String getAirportDep() {
        return airportDep;
    }

    public void setAirportDep(String airportDep) {
        this.airportDep = airportDep;
    }

    public String getAirportArr() {
        return airportArr;
    }

    public void setAirportArr(String airportArr) {
        this.airportArr = airportArr;
    }

    public int getDateDep() {
        return dateDep;
    }

    public void setDateDep(int dateDep) {
        this.dateDep = dateDep;
    }

    public int getDateArr() {
        return dateArr;
    }

    public void setDateArr(int dateArr) {
        this.dateArr = dateArr;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }
}
