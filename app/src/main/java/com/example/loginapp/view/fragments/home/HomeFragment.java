package com.example.loginapp.view.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.loginapp.R;
import com.example.loginapp.adapter.discount_adapter.DiscountAdapter;
import com.example.loginapp.adapter.product_adapter.OnProductClickListener;
import com.example.loginapp.adapter.product_adapter.ProductAdapter;
import com.example.loginapp.databinding.FragmentHomeBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Products;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.HomePresenter;
import com.example.loginapp.utils.Constant;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnProductClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    private NavController navController;

    private HomePresenter presenter;

    private ProductAdapter recommendedAdapter, topChartsAdapter, discountAdapter;

    private DiscountAdapter adapter;

    private ShimmerFrameLayout recommendedProductsPlaceHolder,
            topChartsProductsPlaceHolder,
            discountProductsPlaceHolder,
            userPlaceHolder;

    private RecyclerView recommendedRecyclerview,
            topChartsRecyclerview,
            discountRecyclerView;

    private ConstraintLayout expandRecommendedProductsView,
            expandTopChartsProductsView,
            expandDiscountProductsView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SliderView sliderView;

    private boolean isRefreshing = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new HomePresenter(this);
    }

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        recommendedAdapter = new ProductAdapter(this);
        topChartsAdapter = new ProductAdapter(this);
        discountAdapter = new ProductAdapter(this);
        adapter = new DiscountAdapter(new ArrayList<>());

        recommendedProductsPlaceHolder = binding.recommendedProductsPlaceHolder.getRoot();
        topChartsProductsPlaceHolder = binding.topChartsProductsPlaceHolder.getRoot();
        discountProductsPlaceHolder = binding.discountProductsPlaceHolder.getRoot();
        userPlaceHolder = binding.userPlaceHolder.getRoot();

        expandRecommendedProductsView = binding.expandRecommendedProductsView;
        expandTopChartsProductsView = binding.expandTopChartsProductsView;
        expandDiscountProductsView = binding.expandDiscountProductsView;

        recommendedRecyclerview = binding.recommendedRecyclerview;
        topChartsRecyclerview = binding.topChartsRecyclerview;
        discountRecyclerView = binding.discountRecyclerView;

        swipeRefreshLayout = binding.homeSwipe;
        swipeRefreshLayout.setColorSchemeResources(R.color.free_shipping_color, R.color.cam, R.color.blue, R.color.red);

        sliderView = binding.discountSliderView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.homeScreenContent.getLayoutTransition().setAnimateParentHierarchy(false);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recommendedProductsPlaceHolder = null;
        topChartsProductsPlaceHolder = null;
        discountProductsPlaceHolder = null;
        expandRecommendedProductsView = null;
        expandTopChartsProductsView = null;
        expandDiscountProductsView = null;
        recommendedRecyclerview = null;
        topChartsRecyclerview = null;
        discountRecyclerView = null;
        recommendedAdapter = null;
        swipeRefreshLayout = null;
        topChartsAdapter = null;
        discountAdapter = null;
        userPlaceHolder = null;
        sliderView = null;
        binding = null;
        adapter = null;
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        binding.setHomeFragment(this);

        recommendedRecyclerview.setAdapter(recommendedAdapter);
        topChartsRecyclerview.setAdapter(topChartsAdapter);
        discountRecyclerView.setAdapter(discountAdapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.setSliderAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isRefreshing) {
                isRefreshing = true;
                presenter.getListProductFromNetwork();
            }
        });

        presenter.initData();
    }

    @Override
    public void showRecommendedProducts(List<Product> products) {
        recommendedAdapter.submitList(products);
    }

    @Override
    public void showTopChartsProducts(List<Product> products) {
        topChartsAdapter.submitList(products);
    }

    @Override
    public void showDiscountProducts(List<Product> products) {
        discountAdapter.submitList(products);
    }

    public void onUserAvatarClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.currentUserData);
        navController.navigate(R.id.editUserInformationFragment, bundle);
    }

    @Override
    public void isRecommendedProductsLoading(boolean isLoading) {
        if (isLoading) {
            recommendedProductsPlaceHolder.setVisibility(View.VISIBLE);
            recommendedProductsPlaceHolder.startShimmerAnimation();
            recommendedRecyclerview.setVisibility(View.GONE);
            expandRecommendedProductsView.setVisibility(View.GONE);
        } else {
            recommendedProductsPlaceHolder.stopShimmerAnimation();
            recommendedProductsPlaceHolder.setVisibility(View.GONE);
            recommendedRecyclerview.setVisibility(View.VISIBLE);
            expandRecommendedProductsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isTopChartsProductsLoading(boolean isLoading) {
        if (isLoading) {
            topChartsRecyclerview.setVisibility(View.GONE);
            expandTopChartsProductsView.setVisibility(View.GONE);
            topChartsProductsPlaceHolder.setVisibility(View.VISIBLE);
            topChartsProductsPlaceHolder.startShimmerAnimation();
        } else {
            topChartsProductsPlaceHolder.stopShimmerAnimation();
            topChartsProductsPlaceHolder.setVisibility(View.GONE);
            topChartsRecyclerview.setVisibility(View.VISIBLE);
            expandTopChartsProductsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isDiscountProductsLoading(boolean isLoading) {
        if (isLoading) {
            discountRecyclerView.setVisibility(View.GONE);
            expandDiscountProductsView.setVisibility(View.GONE);
            discountProductsPlaceHolder.setVisibility(View.VISIBLE);
            discountProductsPlaceHolder.startShimmerAnimation();
        } else {
            discountProductsPlaceHolder.stopShimmerAnimation();
            discountProductsPlaceHolder.setVisibility(View.GONE);
            discountRecyclerView.setVisibility(View.VISIBLE);
            expandDiscountProductsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isUserLoading(boolean isLoading) {
        if (isLoading) {
            binding.userView.setVisibility(View.GONE);
            userPlaceHolder.setVisibility(View.VISIBLE);
            userPlaceHolder.startShimmerAnimation();
        } else {
            userPlaceHolder.stopShimmerAnimation();
            userPlaceHolder.setVisibility(View.GONE);
            binding.userView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindRecommendedEveryDay(List<Product> products) {
        adapter.setData(products);
    }

    @Override
    public void setShowUserView(Boolean show) {
        binding.userView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void onExpandRecommendProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.recommendedProducts, R.string.recommended_for_you);
    }

    public void onExpandDiscountProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.discountProducts, R.string.discounted_products_label);
    }

    public void onExpandTopChartProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.topChartsProducts, R.string.bestseller);
    }

    private void navigateToExpandedProductsFragment(List<Product> products, @StringRes int label) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXPAND_PRODUCTS_KEY, new Products(products));
        bundle.putInt(Constant.EXPAND_LABEL_KEY, label);
        navController.navigate(R.id.expandProductsFragment, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addUserDataValueEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setEnabled(true);
        sliderView.startAutoCycle();
    }

    @Override
    public void onPause() {
        super.onPause();
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.clearAnimation();
        sliderView.stopAutoCycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeUserDataValueEventListener();
    }

    @Override
    public void getUserData(UserData userData) {
        binding.setUserData(userData);
    }

    @Override
    public void refreshInvisible() {
        isRefreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        navController.navigate(R.id.action_global_productFragment, bundle);
    }
}
