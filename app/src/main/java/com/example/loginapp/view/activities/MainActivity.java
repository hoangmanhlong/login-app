package com.example.loginapp.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityMainBinding;
import com.example.loginapp.presenter.MainPresenter;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements MainView {
    private final String TAG = MainActivity.class.getName();
    private MainPresenter presenter;
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

    private LinearLayout[] parentViews;

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
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            resetView();
            int currentDestinationId = destination.getId();
            if (currentDestinationId == R.id.homeFragment) {
                binding.homeView.setBackgroundResource(viewBackground);
                binding.homeIcon.setImageResource(R.drawable.ic_home_dark);
                binding.home.setClickable(false);
            } else if (currentDestinationId == R.id.searchProductFragment) {
                binding.searchView.setBackgroundResource(viewBackground);
                binding.searchIcon.setImageResource(R.drawable.ic_search_dark);
                binding.search.setClickable(false);
            } else if (currentDestinationId == R.id.cartFragment) {
                binding.cartView.setBackgroundResource(viewBackground);
                binding.cartIcon.setImageResource(R.drawable.ic_cart_dark);
                binding.cart.setClickable(false);
            } else if (currentDestinationId == R.id.favoriteProductFragment) {
                binding.favoriteView.setBackgroundResource(viewBackground);
                binding.favoriteIcon.setImageResource(R.drawable.favorite);
                binding.favorite.setClickable(false);
            } else if (currentDestinationId == R.id.userProfileFragment) {
                binding.userView.setBackgroundResource(viewBackground);
                binding.userIcon.setImageResource(R.drawable.ic_user_dark);
                binding.user.setClickable(false);
            }
        });
    }

    private void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showPopupDialog();
        presenter = new MainPresenter(this);
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

        parentViews = new LinearLayout[]{
            binding.home,
            binding.search,
            binding.cart,
            binding.favorite,
            binding.user
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

    @Override
    public void onLoadFavoriteProducts(int number) {
        if (number > 0) {
            binding.favoriteNoticeView.setVisibility(View.VISIBLE);
            binding.favoriteNumber.setVisibility(View.VISIBLE);
            binding.favoriteNumber.setText(String.valueOf(number));
        } else {
            binding.favoriteNoticeView.setVisibility(View.GONE);
            binding.favoriteNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadBasket(int number) {
        if (number > 0) {
            binding.cartNoticeView.setVisibility(View.VISIBLE);
            binding.cartNumber.setVisibility(View.VISIBLE);
            binding.cartNumber.setText(String.valueOf(number));
        } else {
            binding.cartNoticeView.setVisibility(View.GONE);
            binding.cartNumber.setVisibility(View.GONE);
        }
    }

    public void showPopupDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_popup);
        ImageView imageView = dialog.findViewById(R.id.ivClosePopup);
        imageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
