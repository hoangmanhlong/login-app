package com.example.loginapp.view.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.commonUI.AppMessage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnProductClickListener {

    private final String TAG = this.toString();

    private FragmentHomeBinding binding;

    private final HomePresenter presenter = new HomePresenter(this);

    private final ProductAdapter recommendedAdapter = new ProductAdapter(this);

    private final ProductAdapter topChartsAdapter = new ProductAdapter(this);

    private final ProductAdapter discountAdapter = new ProductAdapter(this);

    private final DiscountAdapter adapter = new DiscountAdapter(new ArrayList<>());

    private ShimmerFrameLayout recommendedProductsPlaceHolder, topChartsProductsPlaceHolder, discountProductsPlaceHolder, userPlaceHolder;

    private RecyclerView recommendedRecyclerview, topChartsRecyclerview, discountRecyclerView;

    private Button expandRecommendedProductsView, expandTopChartsProductsView, expandDiscountProductsView;


    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setHomeFragment(this);

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

        recommendedRecyclerview.setNestedScrollingEnabled(true);

        SliderView sliderView = binding.discountSliderView;

        recommendedRecyclerview.setAdapter(recommendedAdapter);
        topChartsRecyclerview.setAdapter(topChartsAdapter);
        discountRecyclerView.setAdapter(discountAdapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
        sliderView.setSliderAdapter(adapter);

        presenter.initData();

        SwipeRefreshLayout refreshLayout = binding.homeSwipe;
        refreshLayout.setOnRefreshListener(() -> {
            presenter.getListProductFromNetwork();
            refreshLayout.setRefreshing(false);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showRecommendedProducts(List<Product> products) {
        recommendedAdapter.submitList(products);
        recommendedAdapter.notifyDataSetChanged();
        isRecommendedProductsLoading(false);
    }

    @Override
    public void showTopChartsProducts(List<Product> products) {
        topChartsAdapter.submitList(products);
        isTopChartsProductsLoading(false);
    }

    @Override
    public void showDiscountProducts(List<Product> products) {
        discountAdapter.submitList(products);
        isDiscountProductsLoading(false);
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
            topChartsProductsPlaceHolder.setVisibility(View.VISIBLE);
            topChartsProductsPlaceHolder.startShimmerAnimation();
            topChartsRecyclerview.setVisibility(View.GONE);
            expandTopChartsProductsView.setVisibility(View.GONE);
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
            discountProductsPlaceHolder.setVisibility(View.VISIBLE);
            discountProductsPlaceHolder.startShimmerAnimation();
            discountRecyclerView.setVisibility(View.GONE);
            expandDiscountProductsView.setVisibility(View.GONE);
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
            userPlaceHolder.setVisibility(View.VISIBLE);
            userPlaceHolder.startShimmerAnimation();
            binding.userView.setVisibility(View.GONE);
        } else {
            userPlaceHolder.stopShimmerAnimation();
            userPlaceHolder.setVisibility(View.GONE);
            binding.userView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getBestsellerProducts(List<Product> products) {
        adapter.setData(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setShowUserView(Boolean show) {
        if (show) AppAnimationState.setUserViewState(binding.userView, true);
        else binding.userView.setVisibility(View.GONE);
    }

    public void onExpandRecommendProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.recommendedProducts, "Recommended for you");
    }

    public void onExpandDiscountProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.discountProducts, "Discount");
    }

    public void onExpandTopChartProductsButtonClick() {
        navigateToExpandedProductsFragment(presenter.topChartsProducts, "Top charts");
    }

    private void navigateToExpandedProductsFragment(List<Product> products, String label) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXPAND_PRODUCTS_KEY, new Products(products));
        bundle.putString(Constant.EXPAND_LABEL_KEY, label);
        NavHostFragment.findNavController(this).navigate(R.id.expandProductsFragment, bundle);
    }

    @Override
    public void getUserData(UserData userData) {
        binding.setUserData(userData);
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        NavHostFragment.findNavController(this).navigate(R.id.action_global_productFragment, bundle);
    }
}
