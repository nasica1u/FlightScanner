package com.christophenasica.flyscanner.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Airport implements Parcelable {

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

    public Airport() {}

    protected Airport(Parcel in) {
        code = in.readString();
        lat = in.readString();
        lon = in.readString();
        name = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        woeid = in.readString();
        tz = in.readString();
        phone = in.readString();
        type = in.readString();
        email = in.readString();
        url = in.readString();
        runway_length = in.readString();
        elev = in.readString();
        icao = in.readString();
        direct_flights = in.readString();
        carriers = in.readString();
    }

    public static final Creator<Airport> CREATOR = new Creator<Airport>() {
        @Override
        public Airport createFromParcel(Parcel in) {
            return new Airport(in);
        }

        @Override
        public Airport[] newArray(int size) {
            return new Airport[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRunway_length() {
        return runway_length;
    }

    public void setRunway_length(String runway_length) {
        this.runway_length = runway_length;
    }

    public String getElev() {
        return elev;
    }

    public void setElev(String elev) {
        this.elev = elev;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getDirect_flights() {
        return direct_flights;
    }

    public void setDirect_flights(String direct_flights) {
        this.direct_flights = direct_flights;
    }

    public String getCarriers() {
        return carriers;
    }

    public void setCarriers(String carriers) {
        this.carriers = carriers;
    }

    private String icao;
    private String direct_flights;
    private String carriers;

    public String getFormattedName() {
        return code + " - " + city + " " + "(" + country + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(woeid);
        dest.writeString(tz);
        dest.writeString(phone);
        dest.writeString(type);
        dest.writeString(email);
        dest.writeString(url);
        dest.writeString(runway_length);
        dest.writeString(elev);
        dest.writeString(icao);
        dest.writeString(direct_flights);
        dest.writeString(carriers);
    }
}