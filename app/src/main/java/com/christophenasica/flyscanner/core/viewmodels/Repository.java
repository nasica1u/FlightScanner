package com.christophenasica.flyscanner.core.viewmodels;

import android.arch.lifecycle.MutableLiveData;

import com.christophenasica.flyscanner.data.Flight;
import com.christophenasica.flyscanner.data.FlightPath;
import com.christophenasica.flyscanner.data.FlightState;

import java.util.Calendar;
import java.util.List;

public class Repository {

    private MutableLiveData<Calendar> mDepartureCalendar = new MutableLiveData<>();
    private MutableLiveData<Calendar> mArrivalCalendar = new MutableLiveData<>();

    private MutableLiveData<List<Flight>> mCurrentFlights = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsConnected = new MutableLiveData<>();

    private MutableLiveData<Flight> mCurrentFlight = new MutableLiveData<>(); // non-tracked

    private MutableLiveData<FlightPath> mCurrentFlightPath = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoadingAircraftDetails = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsAircraftPathUpToDate = new MutableLiveData<>();
    private MutableLiveData<FlightState> mCurrentFlightState = new MutableLiveData<>();
    private MutableLiveData<List<Flight>> mFlightsInfoMenu = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHasFailedLoadingDirectInfos = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsDirectInfosUpToDate = new MutableLiveData<>();

    private static Repository mInstance;

    public static synchronized Repository getInstance() {
        if (mInstance == null)
            mInstance = new Repository();
        return mInstance;
    }

    public MutableLiveData<Calendar> getDepartureCalendar() {
        return mDepartureCalendar;
    }

    public MutableLiveData<Calendar> getArrivalCalendar() {
        return mArrivalCalendar;
    }

    public MutableLiveData<List<Flight>> getCurrentFlights() {
        return mCurrentFlights;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public MutableLiveData<Boolean> getIsConnected() {
        return mIsConnected;
    }

    public MutableLiveData<Flight> getCurrentFlight() {
        return mCurrentFlight;
    }

    public MutableLiveData<FlightPath> getCurrentFlightPath() {
        return mCurrentFlightPath;
    }

    public MutableLiveData<Boolean> getIsLoadingAircraftDetails() {
        return mIsLoadingAircraftDetails;
    }

    public MutableLiveData<Boolean> getIsAircraftPathUpToDate() {
        return mIsAircraftPathUpToDate;
    }

    public MutableLiveData<FlightState> getCurrentFlightState() {
        return mCurrentFlightState;
    }

    public MutableLiveData<List<Flight>> getFlightsInfoMenu() {
        return mFlightsInfoMenu;
    }

    public MutableLiveData<Boolean> getHasFailedLoadingDirectInfos() {
        return mHasFailedLoadingDirectInfos;
    }

    public MutableLiveData<Boolean> getIsDirectInfosUpToDate() {
        return mIsDirectInfosUpToDate;
    }
}
