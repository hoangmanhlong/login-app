package com.example.loginapp.view.fragments.coins;

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
import com.example.loginapp.databinding.FragmentCoinsRewardBinding;
import com.example.loginapp.presenter.CoinsRewardPresenter;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;


public class CoinsRewardFragment extends Fragment implements CoinsRewardView {

    private final CoinsRewardPresenter presenter = new CoinsRewardPresenter(this);

    private final String TAG = this.toString();

    private final Calendar calendar = Calendar.getInstance();

    private FragmentCoinsRewardBinding binding;

    private MaterialCalendarView calendarView;

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
        binding.calendarView.setTopbarVisible(false);
//
//        // Lấy ngày đầu tiên của tháng hiện tại
//        Calendar firstDayOfMonth = Calendar.getInstance();
//        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
//        CalendarDay minDate = CalendarDay.from(firstDayOfMonth);
//
//// Lấy ngày cuối cùng của tháng hiện tại
//        Calendar lastDayOfMonth = Calendar.getInstance();
//        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
//        CalendarDay maxDate = CalendarDay.from(lastDayOfMonth);
//
//        calendarView.state().edit()
//
//                .setFirstDayOfWeek(Calendar.WEDNESDAY)
//                .setMinimumDate(CalendarDay.from(2016, 4, Calendar.D))
//                .setMaximumDate(CalendarDay.from(2016, 5, 12))
//                .setCalendarDisplayMode(CalendarMode.WEEKS)
//                .setSaveCurrentPosition(true)
//                .commit();
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

//    private void updateUI(String date) {
//        Calendar selectedCal = Calendar.getInstance();
//        String[] parts = date.split("-");
//        int year = Integer.parseInt(parts[0]);
//        int month = Integer.parseInt(parts[1]) - 1; // Vì Calendar.MONTH bắt đầu từ 0
//        int day = Integer.parseInt(parts[2]);
//        selectedCal.set(year, month, day);
//
//        // Lấy ngày hiện tại
//        Calendar currentCal = Calendar.getInstance();
//        int currentYear = currentCal.get(Calendar.YEAR);
//        int currentMonth = currentCal.get(Calendar.MONTH);
//        int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
//
//        // Cập nhật màu sắc cho ngày đã điểm danh
//        if (year == currentYear && month == currentMonth && day == currentDay) {
//            // Nếu là ngày hiện tại
//            calendarView.setDate(selectedCal.getTimeInMillis(), false, true);
//        } else {
//            // Nếu không phải là ngày hiện tại
//            calendarView.setDate(selectedCal.getTimeInMillis(), false, false);
//        }
//        // Đặt màu nền cho ngày đã điểm danh
//    }
}