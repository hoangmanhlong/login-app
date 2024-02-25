package com.example.loginapp.view.fragments.coins;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.adapter.attendance_adapter.CalendarAdapter;
import com.example.loginapp.databinding.FragmentCoinsRewardBinding;
import com.example.loginapp.presenter.CoinsRewardPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class CoinsRewardFragment extends Fragment implements CoinsRewardView {

    private final CoinsRewardPresenter presenter = new CoinsRewardPresenter(this);

    private final String TAG = this.toString();

    private FragmentCoinsRewardBinding binding;

    private final CalendarAdapter calendarAdapter = new CalendarAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoinsRewardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        presenter.initData();
        List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        binding.calendarRecyclerview.setAdapter(calendarAdapter);
        calendarAdapter.submitList(list);
    }


    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void goVoucherScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_voucherFragment);
    }

    public void onAttendanceButtonClick() {
        presenter.attendance();
    }

    @Override
    public void bindNumberOfCoins(int numberOfCoins) {
        binding.setNumberOfCoins(numberOfCoins);
    }

    @Override
    public void getLastDayOfMonth(String lastDayOfMonth) {
        binding.tvLastDayOfMonth.setText(lastDayOfMonth);
    }
}