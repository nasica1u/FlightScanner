package com.christophenasica.flyscanner.core;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();

    private static final String BASE_URL = "https://opensky-network.org/api";
    private static final String DEPARTURE_URL = "/flights/departure";
    private static final String ARRIVAL_URL = "/flights/arrival";

    private static final String ICAO = "airport";
    private static final String BEGIN_INTERVAL = "begin";
    private static final String END_INTERVAL = "end";

    private static RequestManager requestManager;

    public static RequestManager getInstance() {
        if (requestManager == null)
            requestManager = new RequestManager();
        return requestManager;
    }

    public String get(String sourceUrl, Map<String, String> params) {
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

    private Map<String, String> getParams(String icao, int begin, int end) {
        Map<String, String> params = new HashMap<>();
        params.put(ICAO, icao);
        params.put(BEGIN_INTERVAL, begin+"");
        params.put(END_INTERVAL, end+"");
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
        Map<String, String> params = getParams(icao, begin, end);
        return JsonParser.parseString(get(BASE_URL+DEPARTURE_URL, params)).getAsJsonArray();
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
        Map<String, String> params = getParams(icao, begin, end);
        return JsonParser.parseString(get(BASE_URL+ARRIVAL_URL, params)).getAsJsonArray();
    }

    public void doGetRequestOnFlights(RequestListener requestListener, RequestType requestType, RequestInfos requestInfos) {
        new RequestAsyncTask(requestListener, requestType, requestInfos).execute();
    }

    public static class RequestInfos {
        private String icao;
        private int begin;
        private int end;

        public RequestInfos(String icao, int begin, int end) {
            this.icao = icao;
            this.begin = begin;
            this.end = end;
        }

        @NonNull
        @Override
        public String toString() {
            return "ICAO: " + icao + "\n BEGIN: " + begin + "\n END: "+end;
        }
    }

    public enum RequestType {
        DEPARTURE, ARRIVAL
    }

    public static class RequestAsyncTask extends AsyncTask<Void, Void, JsonArray> {

        private WeakReference<RequestListener> mRequestListener;
        private RequestType mRequestType;
        private RequestInfos mRequestInfos;

        public RequestAsyncTask(RequestListener requestListener, RequestType requestType, RequestInfos requestInfos) {
            mRequestListener = new WeakReference<>(requestListener);
            mRequestType = requestType;
            mRequestInfos = requestInfos;
        }

        @Override
        protected JsonArray doInBackground(Void... voids) {
            JsonArray result = null;
            switch (mRequestType) {
                case DEPARTURE:
                    result = RequestManager.getInstance().getDeparture(mRequestInfos);
                    break;
                case ARRIVAL:
                    result = RequestManager.getInstance().getArrival(mRequestInfos);
                    break;
                default:
                    Log.e(TAG, "Request["+mRequestType+"]: request type not found!");
            }
            return result;
        }

        @Override
        protected void onPostExecute(JsonArray result) {
            super.onPostExecute(result);
            if (result == null)
                mRequestListener.get().onRequestFail("Your request about " + mRequestType.name() + " has failed: " + mRequestInfos);
            else
                mRequestListener.get().onRequestSuccess(result);
        }
    }

    public interface RequestListener {
        void onRequestSuccess(JsonArray jsonArray);
        void onRequestFail(String msg);
    }
}
