package com.christophenasica.flyscanner.core.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.christophenasica.flyscanner.data.Flight;
import com.christophenasica.flyscanner.data.FlightPath;
import com.christophenasica.flyscanner.data.FlightState;

import java.util.List;

public class MapViewModel extends AndroidViewModel {

    private MutableLiveData<FlightPath> mCurrentFlightPath;
    private MutableLiveData<Boolean> mIsLoadingAircraftDetails;
    private MutableLiveData<Boolean> mIsAircraftPathUpToDate;
    private MutableLiveData<FlightState> mCurrentFlightState;
    private MutableLiveData<List<Flight>> mFlightsInfoMenu;
    private MutableLiveData<Boolean> mHasFailedLoadingDirectInfos;
    private MutableLiveData<Boolean> mIsDirectInfosUpToDate;

    public MapViewModel(@NonNull Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        mCurrentFlightPath = repository.getCurrentFlightPath();
        mIsLoadingAircraftDetails = repository.getIsLoadingAircraftDetails();
        mIsAircraftPathUpToDate = repository.getIsAircraftPathUpToDate();
        mCurrentFlightState = repository.getCurrentFlightState();
        mFlightsInfoMenu = repository.getFlightsInfoMenu();
        mHasFailedLoadingDirectInfos = repository.getHasFailedLoadingDirectInfos();
        mIsDirectInfosUpToDate = repository.getIsDirectInfosUpToDate();
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
