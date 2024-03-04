package com.example.loginapp.presenter;

import android.os.Build;
import android.util.Log;

import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DayWithCheck;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.CoinsRewardInterator;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.view.fragments.coins.CoinsRewardView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoinsRewardPresenter implements CoinsRewardListener {

    private final String TAG = this.toString();

    private final CoinsRewardInterator interator = new CoinsRewardInterator(this);

    private AttendanceManager attendanceManager;

    private final List<DayWithCheck> daysInWeek = new ArrayList<>();

    private final List<DayWithCheck> checkedDays = new ArrayList<>();

    private final CoinsRewardView view;

    private Date lastDayOfMonth;

    private boolean tookAttendance = false;

    private List<Voucher> allVouchers = new ArrayList<>();

    private List<Voucher> showedVouchers = new ArrayList<>();

    private int currentDay;

    public CoinsRewardPresenter(CoinsRewardView view) {
        this.view = view;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            currentDay = currentDate.getDayOfMonth();

            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            LocalDate startOfWeek = currentDate.minusDays(dayOfWeek.getValue() - 1);

            for (int i = 0; i < 7; i++) {
                LocalDate date = startOfWeek.plusDays(i);
                daysInWeek.add(new DayWithCheck(date.getDayOfMonth()));
            }

            LocalDate lastDayOfMonthLocalDate = currentDate.withDayOfMonth(LocalDate.now().lengthOfMonth());
            lastDayOfMonth = new Date(
                    lastDayOfMonthLocalDate.getDayOfMonth(),
                    lastDayOfMonthLocalDate.getMonthValue(),
                    lastDayOfMonthLocalDate.getYear()
            );
        }
    }

    public void initData() {
        view.getLastDayOfMonth(lastDayOfMonth);
        if (attendanceManager == null) getAttendanceData();
        else {
            view.isAttendanceLoading(false);
            view.isGetCoinButtonVisible(tookAttendance);
            view.bindNumberOfCoins(attendanceManager.getNumberOfCoins());
            view.bindCheckedDates(checkedDays);
        }
        if (showedVouchers.isEmpty()) getVouchers();
        else view.bindVouchersList(showedVouchers);
    }

    public void redeemVoucher(Voucher voucher) {
        int numberOfCoinsNeededToExchange = voucher.getNumberOfCoinsNeededToExchange();
        if (attendanceManager != null && attendanceManager.getNumberOfCoins() >= numberOfCoinsNeededToExchange) {
            interator.redeemVoucher(
                    voucher,
                    attendanceManager.getNumberOfCoins() - numberOfCoinsNeededToExchange
            );
        } else {
            view.onMessage("You do not have enough points to redeem this voucher");
        }
    }

    public void attendance() {
        if (!tookAttendance) {
            List<Integer> newCheckedDays = new ArrayList<>();

            if (attendanceManager != null) newCheckedDays = attendanceManager.getCheckedDates();

            newCheckedDays.add(currentDay);

            interator.attendance(
                    new AttendanceManager(
                            newCheckedDays,
                            attendanceManager == null ? 100 : attendanceManager.getNumberOfCoins() + 100,
                            lastDayOfMonth
                    )
            );
        }
    }

    private void getVouchers() {
        interator.getAllVouchers();
    }

    private void getAttendanceData() {
        view.isAttendanceLoading(true);
        interator.getAttendanceData();
    }

    @Override
    public void getAttendanceData(AttendanceManager manager) {
        view.isAttendanceLoading(false);
        this.attendanceManager = manager;
        view.bindNumberOfCoins(manager.getNumberOfCoins());
//        view.getLastDayOfMonth(manager.getExpiratioDate());

        List<Integer> checkedDays = manager.getCheckedDates();
        view.bindCheckedDates(getCheckedDays(checkedDays));

        tookAttendance = checkedDays.contains(currentDay);
        view.isGetCoinButtonVisible(tookAttendance);
    }

    private List<DayWithCheck> getCheckedDays(List<Integer> list) {
        checkedDays.clear();
        for (DayWithCheck day : daysInWeek) {
            DayWithCheck checkedDay = new DayWithCheck(day.getDay(), list.contains(day.getDay()));
            checkedDays.add(checkedDay);
        }
        Log.d(TAG, "getCheckedDays: " + checkedDays);
        return checkedDays;
    }

    @Override
    public void iAttendanceSuccess(boolean isSuccess) {

    }

    @Override
    public void isCheckedDayEmpty() {
        view.isAttendanceLoading(false);
        view.bindCheckedDates(daysInWeek);
        view.getLastDayOfMonth(lastDayOfMonth);
        tookAttendance = false;
        view.bindNumberOfCoins(0);
        view.isGetCoinButtonVisible(tookAttendance);
        attendanceManager = null;
    }

    @Override
    public void getDataError() {
        view.isAttendanceLoading(false);
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        Log.d(TAG, "allVouchers: " + vouchers.size());
        this.allVouchers = vouchers;
        interator.getMyVouchers();
    }

    @Override
    public void getMyVouchers(List<Voucher> vouchers) {
        List<Voucher> newVouchersTemp = new ArrayList<>(allVouchers);
        newVouchersTemp.removeAll(vouchers);
        for (Voucher myVoucher : vouchers) {
            // Duyệt qua từng voucher trong danh sách tất cả các voucher
            for (int i = 0; i < newVouchersTemp.size(); i++) {
                Voucher voucher = newVouchersTemp.get(i);
                // So sánh ID của voucher trong danh sách tất cả và myvoucher
                if (Objects.equals(voucher.getVoucherCode(), myVoucher.getVoucherCode())) {
                    // Nếu ID trùng, xóa voucher khỏi danh sách tất cả các voucher
                    newVouchersTemp.remove(i);
                    // Sau khi xóa, giảm chỉ số để không bỏ qua voucher tiếp theo
                    i--;
                }
            }
        }
        showedVouchers = newVouchersTemp;
        view.bindVouchersList(newVouchersTemp);
    }

    @Override
    public void isMyVoucherEmpty() {
        showedVouchers = allVouchers;
        view.bindVouchersList(showedVouchers);
    }

    @Override
    public void isVouchersListEmpty(boolean isEmpty) {
//        view.isVouchersListEmpty(isEmpty);
    }

    @Override
    public void isRedeemSuccess(boolean isSuccess) {

    }
}
