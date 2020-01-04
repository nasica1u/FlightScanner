package com.christophenasica.flyscanner.core.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.christophenasica.flyscanner.data.FlightPath;

public class MapViewModel extends AndroidViewModel {

    private MutableLiveData<FlightPath> mCurrentFlightPath;
    private MutableLiveData<Boolean> mIsLoadingAircraftDetails;
    private MutableLiveData<Boolean> mIsAircraftPathUpToDate;

    public MapViewModel(@NonNull Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        mCurrentFlightPath = repository.getCurrentFlightPath();
        mIsLoadingAircraftDetails = repository.getIsLoadingAircraftDetails();
        mIsAircraftPathUpToDate = repository.getIsAircraftPathUpToDate();
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
}
