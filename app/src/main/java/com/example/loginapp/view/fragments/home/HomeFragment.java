package com.example.loginapp.view.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.loginapp.view.commonUI.AppMessage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnProductClickListener {

    private FragmentHomeBinding binding;

    private final HomePresenter presenter = new HomePresenter(this);

    private final ProductAdapter recommendedAdapter = new ProductAdapter(this);

    private final ProductAdapter topChartsAdapter = new ProductAdapter(this);

    private final ProductAdapter discountAdapter = new ProductAdapter(this);

    private DiscountAdapter adapter;

    private ShimmerFrameLayout recommendedProductsPlaceHolder, topChartsProductsPlaceHolder, discountProductsPlaceHolder, userPlaceHolder;

    private RecyclerView recommendedRecyclerview, topChartsRecyclerview, discountRecyclerView;

    private Button expandRecommendedProductsView, expandTopChartsProductsView, expandDiscountProductsView;

    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setHomeFragment(this);

        recommendedProductsPlaceHolder = binding.recommendedProductsPlaceHolder;
        topChartsProductsPlaceHolder = binding.topChartsProductsPlaceHolder;
        discountProductsPlaceHolder = binding.discountProductsPlaceHolder;
        userPlaceHolder = binding.userPlaceHolder;

        expandRecommendedProductsView = binding.expandRecommendedProductsView;
        expandTopChartsProductsView = binding.expandTopChartsProductsView;
        expandDiscountProductsView = binding.expandDiscountProductsView;

        recommendedRecyclerview = binding.recommendedRecyclerview;
        topChartsRecyclerview = binding.topChartsRecyclerview;
        discountRecyclerView = binding.discountRecyclerView;

        recommendedRecyclerview.setAdapter(recommendedAdapter);
        topChartsRecyclerview.setAdapter(topChartsAdapter);
        discountRecyclerView.setAdapter(discountAdapter);

        SliderView sliderView = binding.discountSliderView;

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();

        adapter = new DiscountAdapter(new ArrayList<>());
        binding.discountSliderView.setSliderAdapter(adapter);

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
        ShimmerFrameLayout placeHolder = binding.recommendedProductsPlaceHolder;
        if (isLoading) {
            recommendedProductsPlaceHolder.setVisibility(View.VISIBLE);
            recommendedProductsPlaceHolder.startShimmerAnimation();
            recommendedRecyclerview.setVisibility(View.GONE);
            expandRecommendedProductsView.setVisibility(View.GONE);
        } else {
            placeHolder.stopShimmerAnimation();
            placeHolder.setVisibility(View.GONE);
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

    public void onExpandRecommendProductsButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXPAND_PRODUCTS_KEY, new Products(presenter.recommendedProducts));
        bundle.putString(Constant.EXPAND_LABEL_KEY, "Recommended for you");
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_expandProductsFragment, bundle);
    }

    public void onExpandDiscountProductsButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXPAND_PRODUCTS_KEY, new Products(presenter.discountProducts));
        bundle.putString(Constant.EXPAND_LABEL_KEY, "Discount");
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_expandProductsFragment, bundle);
    }

    public void onExpandTopChartProductsButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXPAND_PRODUCTS_KEY, new Products(presenter.topChartsProducts));
        bundle.putString(Constant.EXPAND_LABEL_KEY, "Top charts");
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_expandProductsFragment, bundle);
    }

    @Override
    public void getUserData(UserData userData) {
        binding.setUserData(userData);
        isUserLoading(false);
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_productFragment, bundle);
    }
}
