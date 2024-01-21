package com.example.loginapp.view.fragment.home;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.loginapp.R;
import com.example.loginapp.adapter.discount_adapter.DiscountAdapter;
import com.example.loginapp.adapter.product_adapter.HomeAdapter;
import com.example.loginapp.adapter.product_adapter.OnItemClickListener;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.FragmentHomeBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.HomePresenter;
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

    private final HomeAdapter recommendedAdapter = new HomeAdapter(this);

    private final HomeAdapter topChartsAdapter = new HomeAdapter(this);

    private final HomeAdapter discountAdapter = new HomeAdapter(this);

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
        recommendedAdapter.submitList(presenter.recommendedProducts);


        RecyclerView topChartsRecyclerview = binding.topChartsRecyclerview;
        topChartsRecyclerview.setAdapter(topChartsAdapter);
        topChartsAdapter.submitList(presenter.topChartsProducts);

        RecyclerView rvDiscount = binding.rvDiscount;
        rvDiscount.setAdapter(discountAdapter);
        discountAdapter.submitList(presenter.discountProducts);

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

        LinearLayout bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
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

    @Override
    public void getUserData(UserData userData) {
        if (userData.getUsername() == null || userData.getPhotoUrl() == null || userData.getUsername().equals("") || userData.getPhotoUrl().equals("")) {
            binding.userView.setVisibility(View.GONE);

            binding.updateInfoBanner.setTranslationY(-100f);
            binding.updateInfoBanner.setVisibility(View.VISIBLE);
            ObjectAnimator slideDown = ObjectAnimator.ofFloat(binding.updateInfoBanner, "translationY", 0f);
            slideDown.setDuration(500); // Adjust the duration as needed
            // Start the animation
            slideDown.start();
        } else {
//            binding.userView.setVisibility(View.VISIBLE);
            binding.updateInfoBanner.setVisibility(View.GONE);
            // Set initial translationY to -100f (you can adjust this value)
            binding.userView.setTranslationY(-100f);
            binding.userView.setVisibility(View.VISIBLE);

            // Create ObjectAnimator for translationY property
            ObjectAnimator slideDown = ObjectAnimator.ofFloat(binding.userView, "translationY", 0f);
            slideDown.setDuration(600); // Adjust the duration as needed

            // Start the animation
            slideDown.start();
            binding.setUserData(userData);
        }
    }

    @Override
    public void onLoadError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadCategories(List<String> categories) {

    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putInt("productId", product.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productFragment, bundle);
    }

    public void onLaterButtonClick() {
        binding.updateInfoBanner.setVisibility(View.GONE);
    }

    public void onUpdateButtonClick() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_editUserInformationFragment);
    }
}