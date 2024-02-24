package com.example.loginapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loginapp.view.activities.MainActivity;

import java.lang.ref.WeakReference;

public class NetworkChecker {

    private static NetworkChecker instance;

    private final MutableLiveData<Boolean> _networkState = new MutableLiveData<>();

    public final LiveData<Boolean> networkState = _networkState;

    private final WeakReference<MainActivity> activityRef;

    private final String TAG = this.toString();

    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            _networkState.postValue(true);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            _networkState.postValue(false);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
//            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        }
    };

    public NetworkChecker(MainActivity activity) {
        this.activityRef = new WeakReference<>(activity);

        ConnectivityManager connectivityManager = activity.getSystemService(ConnectivityManager.class);

        // check unAvailable when app first run
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        _networkState.postValue(activeNetwork != null);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

    public static synchronized NetworkChecker getInstance(MainActivity activity) {
        if (instance == null || instance.activityRef.get() == null) {
            instance = new NetworkChecker(activity);
        }
        return instance;
    }

    public void unregisterNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activityRef.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
