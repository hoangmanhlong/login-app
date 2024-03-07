package com.example.loginapp.view.activities;

import android.annotation.SuppressLint;
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
import com.example.loginapp.utils.NetworkChecker;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.cart.CartFragment;
import com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment;
import com.example.loginapp.view.fragments.home.HomeFragment;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.example.loginapp.view.fragments.search.SearchProductFragment;
import com.example.loginapp.view.fragments.user_profile.UserProfileFragment;
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

    private NetworkChecker networkChecker;

    private final MutableLiveData<Boolean> _isLogged = new MutableLiveData<>();

    private FirebaseAuth.AuthStateListener authStateListener;

    private boolean backPressedOnce = false;

    private final String TAG = MainActivity.class.getName();

    private final MainPresenter presenter = new MainPresenter(this);

    private ViewPagerAdapter viewPagerAdapter;

    private NavController navController;

    private ActivityMainBinding binding;

    private ViewPager2 viewPager;

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

        authStateListener = firebaseAuth -> _isLogged.setValue(firebaseAuth.getCurrentUser() != null);
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

        _isLogged.observe(this, _isLogged -> {
            if (_isLogged) presenter.getDataOnNavigationBar();
            if (_isLogged && navController.getCurrentDestination().getId() != R.id.firstFragment) {
                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
                    navController.popBackStack();
                navController.navigate(R.id.firstFragment);
            }

            if (!_isLogged) {
                navController.popBackStack(navController.getCurrentDestination().getId(), true);
                navController.navigate(R.id.overviewFragment);
            }
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
            Log.d(TAG, "currentDestination: " + controller.getCurrentDestination().toString());
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
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.viewedFavoritesList(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChecker != null) networkChecker.unregisterNetworkCallback();
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
        TabLayout.Tab shoppingCartTab = appNavigationBar.getTabAt(2);
        BadgeDrawable drawable = shoppingCartTab.getOrCreateBadge();
        if (isShoppingCartEmpty) shoppingCartTab.removeBadge();
        else drawable.setNumber(number);
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

    public static class ViewPagerAdapter extends FragmentStateAdapter {


        private final List<Fragment> fragments = new ArrayList<>(Arrays.asList(
                new HomeFragment(),
                new SearchProductFragment(),
                new CartFragment(),
                new FavoriteProductFragment(),
                new UserProfileFragment()
        ));

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
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

