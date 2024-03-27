package com.example.loginapp.view.activities;

import static com.example.loginapp.utils.Constant.BACK_PRESS_INTERVAL;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ConnectivityManager connectivityManager;

    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();

    private final NetworkRequest networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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

    private boolean backPressedOnce = false;

    private NavController navController;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SplashScreen.installSplashScreen(this); // Apply new Splash API
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupNetworkListener();
        showPopupDialog();
        setupNavigation();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getPreviousBackStackEntry() == null) handleDoubleBackPress();
                else navController.navigateUp();
            }

            private void handleDoubleBackPress() {
                if (backPressedOnce) {
                    finish();
                } else {
                    backPressedOnce = true;
                    Toast.makeText(MainActivity.this, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> backPressedOnce = false, BACK_PRESS_INTERVAL);
                }
            }
        });
    }

    private void setupNetworkListener() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected.postValue(activeNetwork != null);
        isConnected.observe(this, this::updateUiBasedOnConnectivity);
    }

    private void updateUiBasedOnConnectivity(boolean isConnected) {
        if (isConnected) {
            binding.container.setVisibility(View.VISIBLE);
            binding.networkConnectionErrorView.setVisibility(View.GONE);
            binding.shimmerLayout.stopShimmerAnimation();
        } else {
            binding.container.setVisibility(View.GONE);
            binding.networkConnectionErrorView.setVisibility(View.VISIBLE);
            binding.shimmerLayout.startShimmerAnimation();
        }
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.container.getId());
        navController = navHostFragment.getNavController();
    }

    @Override
    public void onStart() {
        super.onStart();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public void showPopupDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_popup);
        dialog.setCancelable(false);
        ImageView imageView = dialog.findViewById(R.id.ivClosePopup);
        imageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
