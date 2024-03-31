package com.example.loginapp.view.fragments.coins;

import android.app.UiModeManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.loginapp.R;
import com.example.loginapp.adapter.attendance_adapter.CalendarAdapter;
import com.example.loginapp.adapter.change_coins_adapter.ChangeCoinsAdapter;
import com.example.loginapp.adapter.change_coins_adapter.OnVoucherClickListener;
import com.example.loginapp.databinding.FragmentCoinsRewardBinding;
import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DayWithCheck;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.CoinsRewardPresenter;
import com.example.loginapp.view.commonUI.AppConfirmDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.List;


public class CoinsRewardFragment extends Fragment implements CoinsRewardView, OnVoucherClickListener {

    private CoinsRewardPresenter presenter;

    private static final String TAG = CoinsRewardFragment.class.getSimpleName();

    private FragmentCoinsRewardBinding binding;

    private CalendarAdapter calendarAdapter;

    private ChangeCoinsAdapter changeCoinsAdapter;

    private ShimmerFrameLayout coinsPlaceHolder, vouchersLoadingView;

    private RecyclerView voucherRecyclerView; // Vouchers List of App

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new CoinsRewardPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoinsRewardBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        coinsPlaceHolder = binding.coinsPlaceHolder;
        vouchersLoadingView = binding.voucherLoadingView;
        voucherRecyclerView = binding.changeCoinsRecyclerView;
        calendarAdapter = new CalendarAdapter();
        changeCoinsAdapter = new ChangeCoinsAdapter(this);
        return binding.getRoot();
    }

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView calendarRecyclerview = binding.calendarRecyclerview;
        calendarRecyclerview.setAdapter(calendarAdapter);
        voucherRecyclerView.setAdapter(changeCoinsAdapter);

        // Remove Effect when item changed/updated
        SimpleItemAnimator calendarRecyclerviewSimpleItemAnimator =
                ((SimpleItemAnimator) calendarRecyclerview.getItemAnimator());
        if (calendarRecyclerviewSimpleItemAnimator != null)
            calendarRecyclerviewSimpleItemAnimator.setSupportsChangeAnimations(false);

        SimpleItemAnimator voucherRecyclerViewSimpleItemAnimator =
                ((SimpleItemAnimator) voucherRecyclerView.getItemAnimator());
        if (voucherRecyclerViewSimpleItemAnimator != null)
            voucherRecyclerViewSimpleItemAnimator.setSupportsChangeAnimations(false);

        presenter.initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addAttendanceDataValueEventListener();
        presenter.addVouchersValueEventListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        changeCoinsAdapter = null;
        calendarAdapter = null;
        voucherRecyclerView = null;
        coinsPlaceHolder = null;
        vouchersLoadingView = null;
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeAttendanceDataValueEventListener();
        presenter.removeMyVouchersValueEventListener();
        presenter.removeVouchersValueEventListener();
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void goVoucherScreen() {
        navController.navigate(R.id.action_coinsRewardFragment_to_voucherFragment);
    }

    public void onAttendanceButtonClick() {
        presenter.attendance();
    }

    @Override
    public void isAttendanceLoading(boolean loading) {
        if (loading) {
            binding.llAttendance.setVisibility(View.GONE);
            coinsPlaceHolder.setVisibility(View.VISIBLE);
            coinsPlaceHolder.startShimmerAnimation();
        } else {
            coinsPlaceHolder.stopShimmerAnimation();
            coinsPlaceHolder.setVisibility(View.GONE);
            binding.llAttendance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindNumberOfCoins(int numberOfCoins) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        String formattedNumber = decimalFormat.format(numberOfCoins);
        binding.setNumberOfCoins(formattedNumber);
    }

    @Override
    public void getLastDayOfMonth(Date date) {
        binding.setDate(date);
    }

    @Override
    public void bindCheckedDates(List<DayWithCheck> dayWithCheckList) {
        calendarAdapter.submitList(dayWithCheckList);
    }

    @Override
    public void isGetCoinButtonVisible(boolean visible) {
        binding.btAttendance.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void bindVouchersList(List<Voucher> vouchers) {
        changeCoinsAdapter.submitList(vouchers);
    }

    @Override
    public void isVouchersListEmpty(boolean isEmpty) {
        binding.setIsVouchersListEmpty(isEmpty);
    }

    @Override
    public void showSnackBar(@StringRes int message) {
        Snackbar.make(binding.getRoot(), getString(message), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(isSystemInDarkMode() ? Color.WHITE : Color.BLACK)
                .setTextColor(isSystemInDarkMode() ? Color.BLACK : Color.WHITE)
                .show();
    }

    private boolean isSystemInDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return requireContext().getSystemService(UiModeManager.class).getNightMode() == UiModeManager.MODE_NIGHT_YES;
        }
        return false;
    }

    @Override
    public void isVouchersLoading(boolean isLoading) {
        if (isLoading) {
            voucherRecyclerView.setVisibility(View.GONE);
            vouchersLoadingView.setVisibility(View.VISIBLE);
            vouchersLoadingView.startShimmerAnimation();
        } else {
            vouchersLoadingView.stopShimmerAnimation();
            vouchersLoadingView.setVisibility(View.GONE);
            voucherRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVoucherClick(Voucher voucher) {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.redeem),
                getString(R.string.change_voucher_title, String.valueOf(voucher.getNumberOfCoinsNeededToExchange())),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        presenter.redeemVoucher(voucher);
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }
        );
    }
}