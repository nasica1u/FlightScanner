package com.christophenasica.flyscanner.core.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.christophenasica.flyscanner.data.Flight;

import java.util.Calendar;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Calendar> mDepartureCalendar;
    private MutableLiveData<Calendar> mArrivalCalendar;

    private MutableLiveData<List<Flight>> mCurrentFlights;
    private MutableLiveData<Boolean> mIsLoading;

    public MainViewModel(Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        mDepartureCalendar = repository.getDepartureCalendar();
        mArrivalCalendar = repository.getArrivalCalendar();
        mCurrentFlights = repository.getCurrentFlights();
        mIsLoading = repository.getIsLoading();
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
