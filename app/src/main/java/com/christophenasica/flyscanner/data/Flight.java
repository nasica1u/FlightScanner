package com.christophenasica.flyscanner.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class Flight implements Parcelable {

    public static final String ICAO24 = "icao24";
    public static final String TIME_DEPARTURE = "firstSeen";
    public static final String DEPARTURE_AIRPORT = "estDepartureAirport";
    public static final String TIME_ARRIVAL = "lastSeen";
    public static final String ARRIVAL_AIRPORT = "estArrivalAirport";
    public static final String CALL_SIGN = "callsign";
    public static final String DEPARTURE_H_DISTANCE = "estDepartureAirportHorizDistance";
    public static final String DEPARTURE_V_DISTANCE = "estDepartureAirportVertDistance";
    public static final String ARRIVAL_H_DISTANCE = "estDepartureAirportHorizDistance";
    public static final String ARRIVAL_V_DISTANCE = "estDepartureAirportVertDistance";
    public static final String DEPARTURE_CANDIDATES_COUNT = "departureAirportCandidatesCount";
    public static final String ARRIVAL_CANDIDATES_COUNT = "arrivalAirportCandidatesCount";

    private String flightName; //icao24
    private int dateDep; //firstSeen
    private String airportDep; //estDepartureAirport
    private int dateArr; //lastSeen
    private String airportArr; //estArrivalAirport
    private String callsign; //callsign
    private int depHorDist;
    private int depVerDist;
    private int arrHorDist;
    private int arrVerDist;
    private int depCandidatesCount;
    private int arrCandidatesCount;

    private int flightTime; //lastSeen-firstSeen is a custom attribute : out of API response

    public Flight(String flightName, int dateDep, String airportDep, int dateArr, String airportArr, String callsign, int depHorDist, int depVerDist, int arrHorDist, int arrVerDist, int depCandidatesCount, int arrCandidatesCount) {
        this.flightName = flightName;
        this.dateDep = dateDep;
        this.airportDep = airportDep;
        this.dateArr = dateArr;
        this.airportArr = airportArr;
        this.callsign = callsign;
        this.depHorDist = depHorDist;
        this.depVerDist = depVerDist;
        this.arrHorDist = arrHorDist;
        this.arrVerDist = arrVerDist;
        this.depCandidatesCount = depCandidatesCount;
        this.arrCandidatesCount = arrCandidatesCount;
        this.flightTime = dateArr-dateDep;
    }

    public Flight(JsonObject jsonObject) {
        flightName = jsonObject.get(Flight.ICAO24) instanceof JsonNull ? "" : jsonObject.get(Flight.ICAO24).getAsString();
        dateDep = jsonObject.get(Flight.TIME_DEPARTURE) instanceof JsonNull ? -1 : jsonObject.get(Flight.TIME_DEPARTURE).getAsInt();
        airportDep = jsonObject.get(Flight.DEPARTURE_AIRPORT) instanceof JsonNull ? "" : jsonObject.get(Flight.DEPARTURE_AIRPORT).getAsString();
        dateArr = jsonObject.get(Flight.TIME_ARRIVAL) instanceof JsonNull ? -1 : jsonObject.get(Flight.TIME_ARRIVAL).getAsInt();
        airportArr = jsonObject.get(Flight.ARRIVAL_AIRPORT) instanceof JsonNull ? "" : jsonObject.get(Flight.ARRIVAL_AIRPORT).getAsString();
        callsign = jsonObject.get(Flight.CALL_SIGN) instanceof  JsonNull ? "" : jsonObject.get(Flight.CALL_SIGN).getAsString();
        depHorDist = jsonObject.get(Flight.DEPARTURE_H_DISTANCE) instanceof JsonNull ? -1 : jsonObject.get(Flight.DEPARTURE_H_DISTANCE).getAsInt();
        depVerDist = jsonObject.get(Flight.DEPARTURE_V_DISTANCE) instanceof JsonNull ? -1 : jsonObject.get(Flight.DEPARTURE_V_DISTANCE).getAsInt();
        arrHorDist = jsonObject.get(Flight.ARRIVAL_H_DISTANCE) instanceof JsonNull ? -1 : jsonObject.get(Flight.ARRIVAL_H_DISTANCE).getAsInt();
        arrVerDist = jsonObject.get(Flight.ARRIVAL_V_DISTANCE) instanceof JsonNull ? -1 : jsonObject.get(Flight.ARRIVAL_V_DISTANCE).getAsInt();
        depCandidatesCount = jsonObject.get(Flight.DEPARTURE_CANDIDATES_COUNT) instanceof JsonNull ? -1 : jsonObject.get(Flight.DEPARTURE_CANDIDATES_COUNT).getAsInt();
        arrCandidatesCount = jsonObject.get(Flight.ARRIVAL_CANDIDATES_COUNT) instanceof JsonNull ? -1 : jsonObject.get(Flight.ARRIVAL_CANDIDATES_COUNT).getAsInt();
        flightTime = dateArr != -1 && dateDep != -1 ? dateArr - dateDep : -1;
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

    private Flight(Parcel in) {
        this.flightName = in.readString();
        this.dateDep = in.readInt();
        this.airportDep = in.readString();
        this.dateArr = in.readInt();
        this.airportArr = in.readString();
        this.callsign = in.readString();
        this.depHorDist = in.readInt();
        this.depVerDist = in.readInt();
        this.arrHorDist = in.readInt();
        this.arrVerDist = in.readInt();
        this.depCandidatesCount = in.readInt();
        this.arrCandidatesCount = in.readInt();
        this.flightTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flightName);
        dest.writeInt(dateDep);
        dest.writeString(airportDep);
        dest.writeInt(dateArr);
        dest.writeString(airportArr);
        dest.writeString(callsign);
        dest.writeInt(depHorDist);
        dest.writeInt(depVerDist);
        dest.writeInt(arrHorDist);
        dest.writeInt(arrVerDist);
        dest.writeInt(depCandidatesCount);
        dest.writeInt(arrCandidatesCount);
        dest.writeInt(flightTime);
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public int getDateDep() {
        return dateDep;
    }

    public void setDateDep(int dateDep) {
        this.dateDep = dateDep;
    }

    public String getAirportDep() {
        return airportDep;
    }

    public void setAirportDep(String airportDep) {
        this.airportDep = airportDep;
    }

    public int getDateArr() {
        return dateArr;
    }

    public void setDateArr(int dateArr) {
        this.dateArr = dateArr;
    }

    public String getAirportArr() {
        return airportArr;
    }

    public void setAirportArr(String airportArr) {
        this.airportArr = airportArr;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public int getDepHorDist() {
        return depHorDist;
    }

    public void setDepHorDist(int depHorDist) {
        this.depHorDist = depHorDist;
    }

    public int getDepVerDist() {
        return depVerDist;
    }

    public void setDepVerDist(int depVerDist) {
        this.depVerDist = depVerDist;
    }

    public int getArrHorDist() {
        return arrHorDist;
    }

    public void setArrHorDist(int arrHorDist) {
        this.arrHorDist = arrHorDist;
    }

    public int getArrVerDist() {
        return arrVerDist;
    }

    public void setArrVerDist(int arrVerDist) {
        this.arrVerDist = arrVerDist;
    }

    public int getDepCandidatesCount() {
        return depCandidatesCount;
    }

    public void setDepCandidatesCount(int depCandidatesCount) {
        this.depCandidatesCount = depCandidatesCount;
    }

    public int getArrCandidatesCount() {
        return arrCandidatesCount;
    }

    public void setArrCandidatesCount(int arrCandidatesCount) {
        this.arrCandidatesCount = arrCandidatesCount;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }
}
