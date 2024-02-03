package com.example.loginapp.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.fragment.product_detail.NewProductInBasketMessage;
import com.example.loginapp.view.fragment.product_detail.NewProductInWishlistMessage;
import com.example.loginapp.view.fragment.select_voucher_fragment.MessageVoucherSelected;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements MainView {

    private final MainPresenter presenter = new MainPresenter(this);

    private NavController navController;

    private ActivityMainBinding binding;

    private final int viewBackground = R.drawable.view_navigation_bar_background;

    private View[] views;

    private ImageView[] imageViews;

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
        super.onCreate(savedInstanceState);
        hasUser();
        initView();
        destinationChangedListener();
    }

    private void hasUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MainActivity.this.finish();
        }
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
                presenter.setViewedShoppingCart(this, true);
            }
            if (currentDestinationId == R.id.favoriteProductFragment) {
                setDestinationUI(views[3], imageViews[3], parentViews[3], R.drawable.favorite);
                presenter.setViewedFavoritesList(this, true);
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
        presenter.setViewedShoppingCart(this, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.setViewedFavoritesList(this, false);
    }

    private void setDestinationUI(View view, ImageView icon, ConstraintLayout navigationItemView, int iconResource) {
        view.setBackgroundResource(viewBackground);
        icon.setImageResource(iconResource);
        navigationItemView.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewProductInWishlistMessage newProductInWishlistMessage = EventBus.getDefault().getStickyEvent(NewProductInWishlistMessage.class);
        if (newProductInWishlistMessage != null)
            EventBus.getDefault().removeStickyEvent(newProductInWishlistMessage);

        NewProductInBasketMessage newProductInBasketMessage = EventBus.getDefault().getStickyEvent(NewProductInBasketMessage.class);
        if (newProductInBasketMessage != null)
            EventBus.getDefault().removeStickyEvent(newProductInBasketMessage);
    }

    private void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter.getStatus(this);
        showPopupDialog();

        binding.setActivity(MainActivity.this);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.mainContainer.getId());
        navController = navHostFragment.getNavController();

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

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    public void showPopupDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_popup);
        dialog.setCancelable(false);
        ImageView imageView = dialog.findViewById(R.id.ivClosePopup);
        imageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

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
}
