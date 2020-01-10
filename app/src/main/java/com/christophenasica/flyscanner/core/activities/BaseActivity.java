package com.christophenasica.flyscanner.core.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.christophenasica.flyscanner.core.NetworkReceiver;
import com.christophenasica.flyscanner.core.viewmodels.ConnectivityViewModel;
import com.christophenasica.flyscanner.core.viewmodels.Repository;

public abstract class BaseActivity extends AppCompatActivity {

    private NetworkReceiver networkReceiver;

    private ConnectivityViewModel mConnectivityViewModel;

    protected boolean mLastIsConnected = false; // used to compare last state and new state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConnectivityViewModel = ViewModelProviders.of(this).get(ConnectivityViewModel.class);
        networkReceiver = new NetworkReceiver(mConnectivityViewModel);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);

        Repository.getInstance().getIsConnected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isConnected) {
                if (isConnected != null) {
                    if (mLastIsConnected != isConnected) {
                        if (!isConnected) {
                            manageConnectionLost();
                        } else {
                            manageConnectionRetrieved();
                        }
                    }
                    mLastIsConnected = isConnected;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null)
            this.unregisterReceiver(networkReceiver);
    }

    /**
     * Called when connection is lost
     */
    protected abstract void manageConnectionLost();

    /**
     * Called when connection is retrieved
     */
    protected abstract void manageConnectionRetrieved();
}
