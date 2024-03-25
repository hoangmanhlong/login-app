package com.example.loginapp.view.fragments.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.adapter.overview_slider_adapter.SliderAdapter;
import com.example.loginapp.databinding.FragmentOverviewBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;

    private NavController navController;

    private SliderView sliderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        sliderView = binding.sliderView;
        binding.setOverviewFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.setSliderAdapter(new SliderAdapter());
    }

    @Override
    public void onStop() {
        super.onStop();
        sliderView.stopAutoCycle();
    }

    @Override
    public void onStart() {
        super.onStart();
        sliderView.startAutoCycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        sliderView = null;
    }

    public void goLoginScreen() {
        navController.navigate(R.id.action_overviewFragment_to_loginFragment);
    }

    public void onCreateAccountBtn() {
        navController.navigate(R.id.action_overviewFragment_to_registerFragment);
    }
}