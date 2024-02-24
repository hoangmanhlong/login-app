package com.example.loginapp.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.R;
import com.example.loginapp.adapter.main_adapter.ViewPagerAdapter;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.utils.NetworkChecker;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements MainView {

    private NetworkChecker networkChecker;

    private boolean backPressedOnce = false;

    private final String TAG = MainActivity.class.getName();

    private final MainPresenter presenter = new MainPresenter(this);

    private ViewPagerAdapter viewPagerAdapter;

    private NavController navController;

    private ActivityMainBinding binding;

    private ViewPager2 viewPager;

//    private final ArrayList<Integer> navigationBarIconFilled = new ArrayList<>(
//            Arrays.asList(
//                    R.drawable.ic_home_dark,
//                    R.drawable.ic_search_dark,
//                    R.drawable.ic_cart_dark,
//                    R.drawable.favorite,
//                    R.drawable.ic_user_dark
//            )
//    );

    private TabLayout appNavigationBar;

    private int startDestinationId;

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
        setNetworkChecker();

        appNavigationBar = binding.bottomNavigation.getRoot();
        viewPager = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this.getLifecycle());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                appNavigationBar.getTabAt(position).select();
            }
        });

        viewPager.setUserInputEnabled(false);

        setupNavigation();

//        presenter.registerAuthStateListener();

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {

            boolean isLogged = firebaseAuth.getCurrentUser() != null;
            Log.d(TAG, "initView: " + isLogged);
            if (isLogged) {
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this.getLifecycle());
                presenter.getDataOnNavigationBar(firebaseAuth.getCurrentUser());
            }
            hasUser(isLogged);
        });
        destinationChangedListener();

        tabSelectedListener();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.container.getId());
        navController = navHostFragment.getNavController();
        startDestinationId = navController.getGraph().getStartDestinationId();
    }

    private void destinationChangedListener() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            currentDestinationId = destination.getId();
            boolean isStartDestination = currentDestinationId == R.id.firstFragment;

            if (isStartDestination && appNavigationBar.getVisibility() == View.GONE)
                AppAnimationState.setBottomNavigationBarState(appNavigationBar, this, true);

            if (!isStartDestination && appNavigationBar.getVisibility() == View.VISIBLE)
                AppAnimationState.setBottomNavigationBarState(appNavigationBar, this, false);

            binding.setIsStartDestination(isStartDestination);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        presenter.unregisterAuthStateListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.viewedFavoritesList(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChecker != null) networkChecker.unregisterNetworkCallback();
        presenter.unregisterAuthStateListener();

        Class<?>[] messageTypes = {NewProductInWishlistMessage.class};
        for (Class<?> messageType : messageTypes) {
            Object message = EventBus.getDefault().getStickyEvent(messageType);
            if (message != null) {
                EventBus.getDefault().removeStickyEvent(message);
            }
        }
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
    public void hasUser(boolean hasUser) {
        if (hasUser) {
            NavDestination navDestination = navController.getCurrentDestination();
            if (navDestination != null) {
                if (currentDestinationId != startDestinationId) {
                    Log.d(TAG, "hasUser: ");
                    navController.popBackStack(currentDestinationId, true);
                    navController.navigate(startDestinationId);
                }
            }
        } else {
            navController.popBackStack(currentDestinationId, true);
            navController.navigate(R.id.overviewFragment);
        }
    }

    @Override
    public void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        TabLayout.Tab shoppingCartTab = appNavigationBar.getTabAt(2);
        if (isShoppingCartEmpty) shoppingCartTab.removeBadge();
        else shoppingCartTab.getOrCreateBadge().setNumber(number);
    }

    @Override
    public void hasNewProductInFavoritesList(boolean hasNewProduct) {
        TabLayout.Tab favoritesListTab = appNavigationBar.getTabAt(3);
        if (hasNewProduct) favoritesListTab.getOrCreateBadge().setText("New");
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

    private void setNetworkChecker() {
        networkChecker = NetworkChecker.getInstance(this);
        networkChecker.networkState.observe(this, isConnected -> {
            binding.setIsConnected(isConnected);
            binding.networkConnectionErrorView.getRoot().setVisibility(isConnected ? View.GONE : View.VISIBLE);
            if (isConnected) {
                binding.networkConnectionErrorView.shimmerLayout.stopShimmerAnimation();
            } else {
                binding.networkConnectionErrorView.shimmerLayout.startShimmerAnimation();
            }
        });
    }

    private void tabSelectedListener() {
        appNavigationBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                if (position == 3) {
                    presenter.viewedFavoritesList(true);
                }
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

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

