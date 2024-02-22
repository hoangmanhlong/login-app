package com.example.loginapp.view.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.utils.NetworkChecker;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MainView {

    private NetworkChecker networkChecker;

    private boolean backPressedOnce = false;

    private final String TAG = MainActivity.class.getName();

    private final MainPresenter presenter = new MainPresenter(this);

    private NavController navController;

    private ActivityMainBinding binding;

    private final ArrayList<Integer> destinationsOfNavigationBar = new ArrayList<>(
            Arrays.asList(
                    R.id.homeFragment,
                    R.id.searchProductFragment,
                    R.id.cartFragment,
                    R.id.favoriteProductFragment,
                    R.id.userProfileFragment
            )
    );

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

    private void initView() {
        setNetworkChecker();

        appNavigationBar = binding.bottomNavigation.getRoot();

        setupNavigation();

        presenter.registerAuthStateListener();

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
            boolean isDestinationInList = destinationsOfNavigationBar.contains(currentDestinationId);
            int isBottomNavigationBarVisible = appNavigationBar.getVisibility();

            if (isDestinationInList && isBottomNavigationBarVisible == View.GONE)
                AppAnimationState.setBottomNavigationBarState(appNavigationBar, this, true);

            if (!isDestinationInList && isBottomNavigationBarVisible == View.VISIBLE)
                AppAnimationState.setBottomNavigationBarState(appNavigationBar, this, false);

            int tabIndex = destinationsOfNavigationBar.indexOf(currentDestinationId);
            if (tabIndex != -1) selectTab(tabIndex);

            if (currentDestinationId == destinationsOfNavigationBar.get(3))
                presenter.viewedFavoritesList(true);
        });
    }

    private void selectTab(int tabIndex) {
        TabLayout.Tab tab = appNavigationBar.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }
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
                if (navDestination.getId() != startDestinationId) {
                    navController.popBackStack(R.id.overviewFragment, true);
                    navController.navigate(startDestinationId);
                }
            }
        } else {
            navController.popBackStack(navController.getCurrentDestination().getId(), true);
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
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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
                    binding.activityContent.setVisibility(isConnected ? View.VISIBLE : View.GONE);
                    binding.networkConnectionErrorView.getRoot().setVisibility(isConnected ? View.GONE : View.VISIBLE);
                }
        );
    }

    private void tabSelectedListener() {
        appNavigationBar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                navController.popBackStack();
                navController.navigate(destinationsOfNavigationBar.get(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
