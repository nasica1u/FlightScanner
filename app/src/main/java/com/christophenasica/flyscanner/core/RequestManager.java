package com.christophenasica.flyscanner.core;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.christophenasica.flyscanner.core.viewmodels.Repository;
import com.christophenasica.flyscanner.data.FlightPath;
import com.christophenasica.flyscanner.data.FlightState;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();

    private static final String BASE_URL = "https://opensky-network.org/api";
    private static final String DEPARTURE_URL = "/flights/departure";
    private static final String ARRIVAL_URL = "/flights/arrival";
    private static final String TRACKS_URL = "/tracks/all";
    private static final String STATES_URL = "/states/all";
    private static final String FLIGHTS_BY_AIRCRAFT_URL = "/flights/aircraft";

    // Departure / Arrival params
    private static final String ICAO = "airport";
    private static final String BEGIN_INTERVAL = "begin";
    private static final String END_INTERVAL = "end";

    // Tracks / States params
    private static final String ICAO24 = "icao24";
    private static final String TIME = "time";

    private static RequestManager requestManager;

    public static RequestManager getInstance() {
        if (requestManager == null)
            requestManager = new RequestManager();
        return requestManager;
    }

    private String get(String sourceUrl, Map<String, String> params) {
        if (Utils.isStringValid(sourceUrl) && params != null) {
            StringBuilder result = new StringBuilder();
            try {
                //Params
                sourceUrl += "?";
                int c = 0;
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (Utils.isStringValid(value)) {
                        if (c != 0) {
                            sourceUrl += "&";
                        }
                        sourceUrl += key + "=" + value;
                        c++;
                    }
                }

                URL url = new URL(sourceUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                Log.i(TAG, "Request[GET]: \n"+"URL: "+sourceUrl+"\nNb Param: "+c);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();

            } catch (IOException e) {
                Log.e(TAG, "Error while doing GET request (url: " + sourceUrl + ") - " + e.getMessage());
            }
        }
        return null;
    }

    // DEPARTURE / ARRIVAL Params
    private Map<String, String> getSearchParams(String icao, int begin, int end) {
        Map<String, String> params = new HashMap<>();
        params.put(ICAO, icao);
        params.put(BEGIN_INTERVAL, begin+"");
        params.put(END_INTERVAL, end+"");
        return params;
    }

    private Map<String, String> getHistoryParams(String icao, int begin, int end) {
        Map<String, String> params = new HashMap<>();
        params.put(ICAO24, icao);
        params.put(BEGIN_INTERVAL, begin+"");
        params.put(END_INTERVAL, end+"");
        return params;
    }

    // TRACKS Params
    private Map<String, String> getTrackParams(String icao24, int time) {
        Map<String, String> params = new HashMap<>();
        params.put(ICAO24, icao24);
        params.put(TIME, time+"");
        return params;
    }

    // STATES Params
    private Map<String, String> getStatesParams(String icao24) {
        Map<String, String> params = new HashMap<>();
        params.put(ICAO24, icao24);
        return params;
    }

    private JsonArray getDeparture(RequestInfos requestInfos) {
        return getDeparture(requestInfos.icao, requestInfos.begin, requestInfos.end);
    }

    /**
     * GET /flights/departure
     * @param icao icao of the airport
     * @param begin Start of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @param end End of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @return See https://opensky-network.org/apidoc/rest.html#departures-by-airport
     */
    private JsonArray getDeparture(String icao, int begin, int end) {
        Map<String, String> params = getSearchParams(icao, begin, end);
        String result = get(BASE_URL+DEPARTURE_URL, params);
        return Utils.isStringValid(result) ? JsonParser.parseString(result).getAsJsonArray() : null;
    }

    private JsonArray getArrival(RequestInfos requestInfos) {
        return getArrival(requestInfos.icao, requestInfos.begin, requestInfos.end);
    }

    /**
     * GET /flights/arrival
     * @param icao icao of the airport
     * @param begin Start of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @param end End of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @return See https://opensky-network.org/apidoc/rest.html#departures-by-airport
     */
    private JsonArray getArrival(String icao, int begin, int end) {
        Map<String, String> params = getSearchParams(icao, begin, end);
        String result = get(BASE_URL+ARRIVAL_URL, params);
        return Utils.isStringValid(result) ? JsonParser.parseString(result).getAsJsonArray() : null;
    }

    /**
     * GET /tracks/all
     * @param icao24 icao of the aircraft
     * @param time Unix time in seconds since epoch, can be any time during the flight (0 means direct informations)
     * @return See https://opensky-network.org/apidoc/rest.html#track-by-aircraft
     */
    private JsonObject getTracks(String icao24, int time) {
        Map<String, String> params = getTrackParams(icao24, time);
        String result = get(BASE_URL + TRACKS_URL, params);
        return Utils.isStringValid(result) ? JsonParser.parseString(result).getAsJsonObject() : null;
    }

    private JsonObject getTracks(RequestInfos requestInfos) {
        return getTracks(requestInfos.icao24, requestInfos.time);
    }

    /**
     * GET /states/all
     * @param icao24 icao of the aircraft
     * @return See https://opensky-network.org/apidoc/rest.html#all-state-vectors
     */
    private JsonObject getStates(String icao24) {
        Map<String, String> params = getStatesParams(icao24);
        String result = get(BASE_URL + STATES_URL, params);
        return Utils.isStringValid(result) ? JsonParser.parseString(result).getAsJsonObject() : null;
    }

    private JsonObject getStates(RequestInfos requestInfos) {
        return getStates(requestInfos.icao24);
    }

    /**
     * GET /flights/aircraft
     * @param icao24 icao of the aircraft
     * @param begin Start of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @param end End of time interval to retrieve flights for as Unix time (seconds since epoch)
     * @return See https://opensky-network.org/apidoc/rest.html#flights-by-aircraft
     */
    private JsonArray getFlightsByAircraft(String icao24, int begin, int end) {
        Map<String, String> params = getHistoryParams(icao24, begin, end);
        String result = get(BASE_URL+FLIGHTS_BY_AIRCRAFT_URL, params);
        return Utils.isStringValid(result) ? JsonParser.parseString(result).getAsJsonArray() : null;
    }

    private JsonArray getFlightsByAircraft(RequestInfos requestInfos) {
        return getFlightsByAircraft(requestInfos.icao24, requestInfos.begin, requestInfos.end);
    }

    public void doGetRequestOnFlights(RequestType requestType, RequestInfos requestInfos) {
        new RequestAsyncTask(requestType, requestInfos).execute();
    }

    public static class RequestInfos {
        private String icao;
        private int begin;
        private int end;

        private String icao24;
        private int time;

        public RequestInfos() {}

        public static RequestInfos initSearchInfos(String icao, int begin, int end) {
            RequestInfos requestInfos = new RequestInfos();
            requestInfos.icao = icao;
            requestInfos.begin = begin;
            requestInfos.end = end;
            return requestInfos;
        }

        public static RequestInfos initStatesInfos(String icao24) {
            RequestInfos requestInfos = new RequestInfos();
            requestInfos.icao24 = icao24;
            return requestInfos;
        }

        public static RequestInfos initTracksInfos(String icao24, int time) {
            RequestInfos requestInfos = new RequestInfos();
            requestInfos.icao24 = icao24;
            requestInfos.time = time;
            return requestInfos;
        }

        public static RequestInfos initHistoryInfos(String icao24, int begin, int end) {
            RequestInfos requestInfos = new RequestInfos();
            requestInfos.icao24 = icao24;
            requestInfos.begin = begin;
            requestInfos.end = end;
            return requestInfos;
        }

        public RequestInfos(int begin, int end, String icao24) {
            this.icao24 = icao24;
            this.begin = begin;
            this.end = end;
        }

        @NonNull
        @Override
        public String toString() {
            String coreInfo = "ICAO: " + icao;
            String additionalInfo = ((begin != -1 && end != -1) ? "\nBEGIN: " + begin + "\nEND: "+end : "") + (time != -1 ? "\nTIME: "+time : "");
            return  coreInfo + additionalInfo;
        }
    }

    public enum RequestType {
        DEPARTURE, ARRIVAL, TRACKS, STATES, FLIGHTS_BY_AIRCRAFT
    }

    public static class RequestAsyncTask extends AsyncTask<Void, Void, Object> {

        private RequestType mRequestType;
        private RequestInfos mRequestInfos;

        public RequestAsyncTask(RequestType requestType, RequestInfos requestInfos) {
            mRequestType = requestType;
            mRequestInfos = requestInfos;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            Object result = null;
            switch (mRequestType) {
                case DEPARTURE:
                    result = RequestManager.getInstance().getDeparture(mRequestInfos);
                    break;
                case ARRIVAL:
                    result = RequestManager.getInstance().getArrival(mRequestInfos);
                    break;
                case TRACKS:
                    result = RequestManager.getInstance().getTracks(mRequestInfos);
                    break;
                case STATES:
                    result = RequestManager.getInstance().getStates(mRequestInfos);
                    break;
                case FLIGHTS_BY_AIRCRAFT:
                    result = RequestManager.getInstance().getFlightsByAircraft(mRequestInfos);
                    break;
                default:
                    Log.e(TAG, "Request["+mRequestType+"]: request type not found!");
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            switch (mRequestType) {
                case DEPARTURE:
                case ARRIVAL:
                    if (result != null)
                        Repository.getInstance().getCurrentFlights().postValue(Utils.convertFlightsJsonArrayToList((JsonArray) result));
                    else
                        Repository.getInstance().getIsLoading().postValue(false);
                    break;
                case TRACKS:
                    if (result != null)
                        Repository.getInstance().getCurrentFlightPath().postValue(new FlightPath((JsonObject) result));
                    break;
                case STATES:
                    if (result != null)
                        Repository.getInstance().getCurrentFlightState().postValue(new FlightState((JsonObject) result));
                    else
                        Repository.getInstance().getHasFailedLoadingDirectInfos().postValue(true);
                    break;
                case FLIGHTS_BY_AIRCRAFT:
                    if (result != null)
                        Repository.getInstance().getFlightsInfoMenu().postValue(Utils.convertFlightsJsonArrayToList((JsonArray) result));
                    break;
                default:
                    Log.e(TAG, "Request["+mRequestType+"]: request type not found!");
            }
        }
    }
}
