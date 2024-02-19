package com.example.loginapp.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.fragments.product_detail.NewProductInBasketMessage;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements MainView {

    private final String TAG = MainActivity.class.getName();

    private final MainPresenter presenter = new MainPresenter(this);

    private NavController navController;

    private ActivityMainBinding binding;

    @DrawableRes
    private final int viewBackground = R.drawable.view_navigation_bar_background;

    private View[] views;

    private ImageView[] imageViews;

    @DrawableRes
    private final int[] iconGray = {
            R.drawable.ic_home_gray,
            R.drawable.ic_search_gray,
            R.drawable.ic_cart_gray,
            R.drawable.ic_favorite_gray,
            R.drawable.ic_user_gray
    };

    private int[] destinations;

    private ConstraintLayout[] parentViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void destinationChangedListener() {
        LinearLayout bottomNavigationBar = binding.bottomNavigation;
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int isBottomNavigationBarVisible = bottomNavigationBar.getVisibility();
            resetView();
            int currentDestinationId = destination.getId();
            boolean isDestinationInList = false;
            for (int i : destinations) {
                if (currentDestinationId == i) {
                    isDestinationInList = true;
                    break;
                }
            }

            if (isDestinationInList && isBottomNavigationBarVisible == View.GONE) {
                AppAnimationState.setBottomNavigationBarState(bottomNavigationBar, this, true);
            } else if (!isDestinationInList && isBottomNavigationBarVisible == View.VISIBLE) {
                AppAnimationState.setBottomNavigationBarState(bottomNavigationBar, this, false);
            }
            if (currentDestinationId == R.id.homeFragment)
                setDestinationUI(views[0], imageViews[0], parentViews[0], R.drawable.ic_home_dark);
            if (currentDestinationId == R.id.searchProductFragment)
                setDestinationUI(views[1], imageViews[1], parentViews[1], R.drawable.ic_search_dark);
            if (currentDestinationId == R.id.cartFragment) {
                setDestinationUI(views[2], imageViews[2], parentViews[2], R.drawable.ic_cart_dark);
                presenter.setViewedShoppingCart(true);
            }
            if (currentDestinationId == R.id.favoriteProductFragment) {
                setDestinationUI(views[3], imageViews[3], parentViews[3], R.drawable.favorite);
                presenter.setViewedFavoritesList(true);
            }
            if (currentDestinationId == R.id.userProfileFragment)
                setDestinationUI(views[4], imageViews[4], parentViews[4], R.drawable.ic_user_dark);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getBasketStatus(NewProductInBasketMessage message) {
        presenter.setViewedShoppingCart(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.setViewedFavoritesList(false);
    }

    private void setDestinationUI(View view, ImageView icon, ConstraintLayout navigationItemView, int iconResource) {
        view.setBackgroundResource(viewBackground);
        icon.setImageResource(iconResource);
        navigationItemView.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Class<?>[] messageTypes = {NewProductInWishlistMessage.class, NewProductInBasketMessage.class};
        for (Class<?> messageType : messageTypes) {
            Object message = EventBus.getDefault().getStickyEvent(messageType);
            if (message != null) {
                EventBus.getDefault().removeStickyEvent(message);
            }
        }
    }

    private void initView() {
        binding.setActivity(this);
//        showPopupDialog();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        navController = navHostFragment.getNavController();
        presenter.currentUserState();
        presenter.getStatus();
        views = new View[]{
                binding.homeView,
                binding.searchView,
                binding.cartView,
                binding.favoriteView,
                binding.userView
        };

        imageViews = new ImageView[]{
                binding.homeIcon,
                binding.searchIcon,
                binding.cartIcon,
                binding.favoriteIcon,
                binding.userIcon
        };

        parentViews = new ConstraintLayout[]{
                binding.home,
                binding.search,
                binding.cart,
                binding.favorite,
                binding.user
        };

        destinations = new int[]{
                R.id.homeFragment,
                R.id.searchProductFragment,
                R.id.cartFragment,
                R.id.favoriteProductFragment,
                R.id.userProfileFragment
        };
        destinationChangedListener();
    }

    public void onHomeClick() {
        navController.navigate(R.id.homeFragment);
    }

    public void onSearchClick() {
        navController.navigate(R.id.searchProductFragment);
    }

    public void onCartClick() {
        navController.navigate(R.id.cartFragment);
    }

    public void onFavoriteClick() {
        navController.navigate(R.id.favoriteProductFragment);
    }

    public void onUserClick() {
        navController.navigate(R.id.userProfileFragment);
    }

    private void resetView() {
        for (int i = 0; i < 5; i++) {
            views[i].setBackgroundResource(android.R.color.transparent);
            imageViews[i].setImageResource(iconGray[i]);
            parentViews[i].setClickable(true);
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
    public void notifyCartChanged(Boolean show) {
        if (show) binding.cartStatusImageView.setVisibility(View.GONE);
        else binding.cartStatusImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyFavoriteChanged(Boolean show) {
        if (show) binding.favoriteStatusImageView.setVisibility(View.GONE);
        else binding.favoriteStatusImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hasUser(Boolean hasUser) {
        int startDestination = navController.getGraph().getStartDestination();
        if (hasUser) {
            NavDestination navDestination = navController.getCurrentDestination();
            if (navDestination != null) {
                if (navDestination.getId() != startDestination) {
                    navController.popBackStack(R.id.overviewFragment, true);
                    navController.navigate(startDestination);
                }
            }
        } else {
            navController.popBackStack(startDestination, true);
            navController.navigate(R.id.overviewFragment);
        }
    }
}
