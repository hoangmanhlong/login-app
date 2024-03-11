package com.example.loginapp.view.activities;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.cart.CartFragment;
import com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment;
import com.example.loginapp.view.fragments.home.HomeFragment;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.example.loginapp.view.fragments.search.SearchProductFragment;
import com.example.loginapp.view.fragments.user_profile.UserProfileDetailFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

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

    private final List<Fragment> listOfVerifiedDestinations = new ArrayList<>(Arrays.asList(
            new HomeFragment(),
            new SearchProductFragment(),
            new CartFragment(),
            new FavoriteProductFragment(),
            new UserProfileDetailFragment()
    ));

    private final List<Fragment> listOfUnconfirmedDestinations = new ArrayList<>(Arrays.asList(
            new HomeFragment(),
            new SearchProductFragment()
    ));

    private final FirebaseAuth.AuthStateListener authStateListener
            = firebaseAuth -> isLogged.setValue(firebaseAuth.getCurrentUser() != null);;

    private boolean backPressedOnce = false;

    private final String TAG = MainActivity.class.getName();

    private final MainPresenter presenter = new MainPresenter(this);

    private NavController navController;

    private ActivityMainBinding binding;

    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private TabLayout navigationBar;

    private int currentDestinationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initView() {
        setupNetworkListener();

        isLogged.observe(this, _isLogged -> {
            if (_isLogged) {
                viewPagerAdapter = new ViewPagerAdapter(
                        getSupportFragmentManager(),
                        getLifecycle(),
                        listOfVerifiedDestinations
                );
                viewPager.setAdapter(viewPagerAdapter);
                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setOffscreenPageLimit(listOfVerifiedDestinations.size());
                presenter.addValueEventListener();
                viewPager.setUserInputEnabled(false);
            } else {
                viewPagerAdapter = new ViewPagerAdapter(
                        getSupportFragmentManager(),
                        getLifecycle(),
                        listOfUnconfirmedDestinations
                );
                viewPager.setAdapter(viewPagerAdapter);
                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setOffscreenPageLimit(listOfUnconfirmedDestinations.size());
                viewPager.setUserInputEnabled(false);
            }
        });

        navigationBar = binding.bottomNavigation.getRoot();
        viewPager = binding.viewPager;

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navigationBar.getTabAt(position).select();
            }
        });

        setupNavigation();

        destinationChangedListener();

        tabSelectedListener();
    }

    private void setupNetworkListener() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Kiểm tra tính khả dụng của mạng khi ứng dụng được khởi chạy
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected.postValue(activeNetwork != null);
        isConnected.observe(this, isConnected -> {
            binding.setIsConnected(isConnected);
            binding.networkConnectionErrorView.getRoot().setVisibility(isConnected ? View.GONE : View.VISIBLE);
            if (isConnected) {
                binding.networkConnectionErrorView.shimmerLayout.stopShimmerAnimation();
            } else {
                binding.networkConnectionErrorView.shimmerLayout.startShimmerAnimation();
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
            currentDestinationId = destination.getId();
            boolean isStartDestination = currentDestinationId == controller.getGraph().getStartDestinationId();

            if (isStartDestination && navigationBar.getVisibility() == View.GONE)
                AppAnimationState.setBottomNavigationBarState(navigationBar, this, true);

            if (!isStartDestination && navigationBar.getVisibility() == View.VISIBLE)
                AppAnimationState.setBottomNavigationBarState(navigationBar, this, false);

            binding.setIsStartDestination(isStartDestination);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        connectivityManager.unregisterNetworkCallback(networkCallback);

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.viewedFavoritesList(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewProductInWishlistMessage message = EventBus.getDefault().getStickyEvent(NewProductInWishlistMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
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
    public void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        TabLayout.Tab shoppingCartTab = navigationBar.getTabAt(2);
        BadgeDrawable drawable = shoppingCartTab.getOrCreateBadge();
        if (isShoppingCartEmpty) shoppingCartTab.removeBadge();
        else drawable.setNumber(number);
    }

    @Override
    public void hasNewProductInFavoritesList(boolean hasNewProduct) {
        TabLayout.Tab favoritesListTab = navigationBar.getTabAt(3);
        if (hasNewProduct) favoritesListTab.getOrCreateBadge();
        else favoritesListTab.removeBadge();
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp() || navController.navigateUp();
    }

    private void tabSelectedListener() {
        navigationBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                if (position == 3) presenter.viewedFavoritesList(true);
                int res = 0;
                switch (position) {
                    case 0:
                        res = R.drawable.ichome;
                        break;
                    case 1:
                        res = R.drawable.ic_search_dark;
                        break;
                    case 2:
                        res = R.drawable.ic_cart_dark;
                        break;
                    case 3:
                        res = R.drawable.favorite;
                        break;
                    case 4:
                        res = R.drawable.ic_user_dark;
                        break;
                }
                tab.setIcon(res);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                int res = 0;
                switch (position) {
                    case 0:
                        res = R.drawable.ic_home_gray;
                        break;
                    case 1:
                        res = R.drawable.ic_search_gray;
                        break;
                    case 2:
                        res = R.drawable.ic_cart_gray;
                        break;
                    case 3:
                        res = R.drawable.ic_favorite_gray;
                        break;
                    case 4:
                        res = R.drawable.ic_user_gray;
                        break;
                }
                tab.setIcon(res);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

    }
}

