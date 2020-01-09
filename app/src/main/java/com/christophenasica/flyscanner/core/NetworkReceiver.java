package com.christophenasica.flyscanner.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.christophenasica.flyscanner.core.viewmodels.ConnectivityViewModel;
import com.christophenasica.flyscanner.core.viewmodels.Repository;

public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkReceiver.class.getSimpleName();

    private ConnectivityViewModel connectivityViewModel;

    public NetworkReceiver(ConnectivityViewModel viewModel) {
        connectivityViewModel = viewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null) {
            Repository.getInstance().getIsConnected().postValue(true);
            Log.i(TAG, "Connection found.");
        }
        else {
            Repository.getInstance().getIsConnected().postValue(false);
            Log.i(TAG, "Connection Lost.");
        }
    }
}
