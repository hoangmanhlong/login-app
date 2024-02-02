package com.example.loginapp.view.fragment.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.loginapp.R;
import com.example.loginapp.adapter.discount_adapter.DiscountAdapter;
import com.example.loginapp.adapter.product_adapter.OnItemClickListener;
import com.example.loginapp.adapter.product_adapter.ProductAdapter;
import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentHomeBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Products;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.HomePresenter;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.AppMessage;
import com.example.loginapp.view.LoadingDialog;
import com.example.loginapp.view.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnItemClickListener {

    private FragmentHomeBinding binding;

    private final HomePresenter presenter = new HomePresenter(this);

    private final ProductAdapter recommendedAdapter = new ProductAdapter(this);

    private final ProductAdapter topChartsAdapter = new ProductAdapter(this);

    private final ProductAdapter discountAdapter = new ProductAdapter(this);

    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }
    }

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
        dialog = LoadingDialog.getLoadingDialog(requireContext());

        RecyclerView recommendedRecyclerview = binding.recommendedRecyclerview;
        recommendedRecyclerview.setAdapter(recommendedAdapter);

        RecyclerView topChartsRecyclerview = binding.topChartsRecyclerview;
        topChartsRecyclerview.setAdapter(topChartsAdapter);


        RecyclerView rvDiscount = binding.rvDiscount;
        rvDiscount.setAdapter(discountAdapter);

        presenter.iniData();

        SwipeRefreshLayout refreshLayout = binding.homeSwipe;
        refreshLayout.setOnRefreshListener(() -> {
            presenter.getListProductFromNetwork();
            refreshLayout.setRefreshing(false);
        });

        SliderView sliderView = binding.discountSliderView;
        DiscountAdapter adapter = new DiscountAdapter();
        binding.discountSliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
    }

    @Override
    public void showProcessBar(Boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
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
        ConstraintLayout userView = binding.userView;
        ConstraintLayout updateInfoBanner = binding.updateInfoBanner;
        if (userData.getUsername() == null || userData.getPhotoUrl() == null || userData.getUsername().equals("") || userData.getPhotoUrl().equals("")) {
            AppAnimationState.setUserViewState(userView, requireActivity(), false);
            AppAnimationState.setUserViewState(updateInfoBanner, requireActivity(), true);
        } else {
            AppAnimationState.setUserViewState(userView, requireActivity(), true);
            AppAnimationState.setUserViewState(updateInfoBanner, requireActivity(), false);
            binding.setUserData(userData);
        }
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productFragment, bundle);
    }

    public void onLaterButtonClick() {
        ConstraintLayout updateInfoBanner = binding.updateInfoBanner;
        AppAnimationState.setUserViewState(updateInfoBanner, requireActivity(), false);
    }

    public void onUpdateButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.currentUserData);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_editUserInformationFragment, bundle);
    }
}