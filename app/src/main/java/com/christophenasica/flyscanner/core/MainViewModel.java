package com.christophenasica.flyscanner.core;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.christophenasica.flyscanner.data.Flight;

import java.util.Calendar;
import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Calendar> mDepartureCalendar;
    private MutableLiveData<Calendar> mArrivalCalendar;

    private MutableLiveData<List<Flight>> mCurrentFlights;
    private MutableLiveData<Boolean> mIsLoading;

    public MainViewModel() {
        mDepartureCalendar = new MutableLiveData<>();
        mArrivalCalendar = new MutableLiveData<>();
        mCurrentFlights = new MutableLiveData<>();
        mIsLoading = new MutableLiveData<>();
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
