package com.christophenasica.flyscanner.core;

import android.arch.lifecycle.MutableLiveData;

import com.christophenasica.flyscanner.data.Flight;

import java.util.Calendar;
import java.util.List;

public class Repository {

    private MutableLiveData<Calendar> mDepartureCalendar = new MutableLiveData<>();
    private MutableLiveData<Calendar> mArrivalCalendar = new MutableLiveData<>();

    private MutableLiveData<List<Flight>> mCurrentFlights = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

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
}
