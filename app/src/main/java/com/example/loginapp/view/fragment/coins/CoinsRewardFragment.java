package com.example.loginapp.view.fragment.coins;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentCoinsRewardBinding;
import com.example.loginapp.model.entity.Coin;
import com.example.loginapp.presenter.CoinsRewardPresenter;
import com.example.loginapp.view.AppAnimationState;

import java.util.Calendar;


public class CoinsRewardFragment extends Fragment implements CoinsRewardView  {

    private final CoinsRewardPresenter presenter = new CoinsRewardPresenter(this);

    private final String TAG = this.toString();

    private final Calendar calendar = Calendar.getInstance();

    private FragmentCoinsRewardBinding binding;

    private CalendarView calendarView;

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
        initView();
        presenter.getCalendar();
    }

    private void initView() {
        setupCalendar();
    }

    private void setupCalendar() {
        calendarView = binding.calendarView;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long minDate = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long maxDate = calendar.getTimeInMillis();
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(maxDate);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void goVoucherScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_coinsRewardFragment_to_voucherFragment);
    }

    @Override
    public void setCoin(Coin coin) {
        Log.d(TAG, "setCoin: " + coin.getEndDate());
        binding.setCoin(coin);
    }

    public void onAttendanceButtonClick() {
        presenter.attendance();
    }
}