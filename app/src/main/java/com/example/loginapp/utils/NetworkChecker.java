package com.example.loginapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.ref.WeakReference;

public class NetworkChecker {

    private static NetworkChecker instance;

    private final MutableLiveData<Boolean> _networkState = new MutableLiveData<>();
    public final LiveData<Boolean> networkState = _networkState;

    private final WeakReference<Context> contextRef;

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
            // Không làm gì trong phương thức này hiện tại
        }
    };

    private NetworkChecker(Context context) {
        this.contextRef = new WeakReference<>(context.getApplicationContext());

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Kiểm tra tính khả dụng của mạng khi ứng dụng được khởi chạy
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        _networkState.postValue(activeNetwork != null);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public static synchronized NetworkChecker getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkChecker(context);
        }
        return instance;
    }

    public void unregisterNetworkCallback() {
        Context context = contextRef.get();
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }
    }
}

