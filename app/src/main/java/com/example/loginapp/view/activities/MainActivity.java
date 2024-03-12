package com.example.loginapp.view.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;

    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLogged = new MutableLiveData<>();

    private final NetworkRequest networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build();

    private final ConnectivityManager.NetworkCallback networkCallback =
            new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    isConnected.postValue(true);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    isConnected.postValue(false);
                }
            };

    private final FirebaseAuth.AuthStateListener authStateListener
            = firebaseAuth -> isLogged.setValue(firebaseAuth.getCurrentUser() != null);

    private boolean backPressedOnce = false;

    private final String TAG = MainActivity.class.getName();

    private NavController navController;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupNetworkListener();
        setupNavigation();
        destinationChangedListener();
    }

    private void setupNetworkListener() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Kiểm tra tính khả dụng của mạng khi ứng dụng được khởi chạy
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected.postValue(activeNetwork != null);
        isConnected.observe(this, isConnected -> {
            binding.setIsConnected(isConnected);
            if (isConnected) {
                binding.networkConnectionErrorView.setVisibility(View.GONE);
                binding.shimmerLayout.stopShimmerAnimation();
            } else {
                binding.networkConnectionErrorView.setVisibility(View.VISIBLE);
                binding.shimmerLayout.startShimmerAnimation();
            }
        });
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.container.getId());
        navController = navHostFragment.getNavController();
    }

    private void destinationChangedListener() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.d(TAG, "getPreviousBackStackEntry: " + navController.getPreviousBackStackEntry());
            Log.d(TAG, "getCurrentDestination: " + controller.getCurrentDestination());

//            if (isStartDestination && navigationBar.getVisibility() == View.GONE)
//                AppAnimationState.setBottomNavigationBarState(navigationBar, this, true);
//
//            if (!isStartDestination && navigationBar.getVisibility() == View.VISIBLE)
//                AppAnimationState.setBottomNavigationBarState(navigationBar, this, false);
//
//            binding.setIsStartDestination(isStartDestination);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

//    public void showPopupDialog() {
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.layout_popup);
//        dialog.setCancelable(false);
//        ImageView imageView = dialog.findViewById(R.id.ivClosePopup);
//        imageView.setOnClickListener(v -> dialog.dismiss());
//        dialog.show();
//    }

    @Override
    public void onBackPressed() {
        if (navController.getPreviousBackStackEntry() == null) {
            if (backPressedOnce) {
                super.onBackPressed();
            } else {
                backPressedOnce = true;
                Toast.makeText(this, this.getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> backPressedOnce = false, Constant.BACK_PRESS_INTERVAL);
            }
        } else {
            super.onBackPressed();
        }
    }
}

