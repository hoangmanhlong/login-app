package com.example.loginapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.adapter.SliderAdapter;
import com.example.loginapp.databinding.FragmentOverviewBinding;
import com.example.loginapp.presenter.OverviewPresenter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class OverviewFragment extends Fragment {
    private FragmentOverviewBinding binding;
    private SliderView sliderView;
    private OverviewPresenter overviewPresenter;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        Log.d(this.toString(), "Created");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setOverviewFragment(this);

        overviewPresenter = new OverviewPresenter(this);

        sliderView = binding.sliderView;
        SliderAdapter adapter = new SliderAdapter();
        binding.sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();

    }

    public void goHomeScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_overviewFragment_to_homeFragment);
    }

    public void goLoginScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("email", "");
        bundle.putString("password", "");
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_overviewFragment_to_loginFragment, bundle);
    }

    public void onCreateAccountBtn() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_overviewFragment_to_registerFragment);
    }
}