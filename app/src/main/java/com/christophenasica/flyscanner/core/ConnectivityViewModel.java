package com.christophenasica.flyscanner.core;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

public class ConnectivityViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mIsConnected;

    public ConnectivityViewModel(Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        mIsConnected = repository.getIsConnected();
    }

    public MutableLiveData<Boolean> getIsConnected() {
        return mIsConnected;
    }
}
