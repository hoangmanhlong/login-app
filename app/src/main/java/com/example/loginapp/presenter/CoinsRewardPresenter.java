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

public class CoinsRewardPresenter implements CoinsRewardListener {

    private boolean wasCoinsTakenForTheFirstTime = false;

    private boolean tookTheVoucherListForTheFirstTime = false;

    private final String TAG = this.toString();

    private CoinsRewardInterator interator = new CoinsRewardInterator(this);

    private AttendanceManager attendanceManager;

    private List<DayWithCheck> daysInWeek = new ArrayList<>();

    private List<DayWithCheck> checkedDays = new ArrayList<>();

    private CoinsRewardView view;

    private Date lastDayOfMonth;

    private boolean tookAttendance = false;

    private List<Voucher> listOfAllOriginalVouchers = new ArrayList<>();

    private List<Voucher> voucherListHasBeenFiltered = new ArrayList<>();

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

    public void clear() {
        view = null;
        interator = null;
        attendanceManager = null;
        daysInWeek = null;
        checkedDays = null;
        lastDayOfMonth = null;
        listOfAllOriginalVouchers = null;
        voucherListHasBeenFiltered = null;
    }

    public void initData() {
        view.getLastDayOfMonth(lastDayOfMonth);
        if (wasCoinsTakenForTheFirstTime) {
            view.isAttendanceLoading(false);
            if (attendanceManager == null) {
                isAttendanceDataEmpty();
            } else {
                getAttendanceData(attendanceManager);
            }
        }

        if (tookTheVoucherListForTheFirstTime) {
            view.isVouchersLoading(false);
            if (!voucherListHasBeenFiltered.isEmpty()) view.bindVouchersList(voucherListHasBeenFiltered);
        }
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

    public void addAttendanceDataValueEventListener() {
        view.isAttendanceLoading(true);
        interator.addAttendanceDataValueEventListener();
    }

    public void removeAttendanceDataValueEventListener() {
        interator.removeAttendanceDataValueEventListener();
    }

    public void addVouchersValueEventListener() {
        view.isVouchersLoading(true);
        interator.addVouchersValueEventListener();
    }

    public void removeVouchersValueEventListener() {
        interator.removeVouchersValueEventListener();
    }

    public void addMyVouchersValueEventListener() {
        interator.addMyVouchersValueEventListener();
    }

    public void removeMyVouchersValueEventListener() {
        interator.removeMyVouchersValueEventListener();
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

    @Override
    public void getAttendanceData(AttendanceManager manager) {
        view.isAttendanceLoading(false);
        this.attendanceManager = manager;
        view.bindNumberOfCoins(manager.getNumberOfCoins());

        List<Integer> checkedDays = manager.getCheckedDates();
        view.bindCheckedDates(getCheckedDays(checkedDays));

        tookAttendance = checkedDays.contains(currentDay);
        view.isGetCoinButtonVisible(tookAttendance);
        wasCoinsTakenForTheFirstTime = true;
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
    public void isAttendanceDataEmpty() {
        view.isAttendanceLoading(false);
        view.bindCheckedDates(daysInWeek);
        view.getLastDayOfMonth(lastDayOfMonth);
        tookAttendance = false;
        view.bindNumberOfCoins(0);
        view.isGetCoinButtonVisible(tookAttendance);
        attendanceManager = null;
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        this.listOfAllOriginalVouchers = vouchers;
        addMyVouchersValueEventListener();
    }

    @Override
    public void getMyVouchers(List<Voucher> vouchers) {
        List<Voucher> newVouchersTemp = new ArrayList<>(listOfAllOriginalVouchers);
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
        voucherListHasBeenFiltered = newVouchersTemp;
        view.isVouchersLoading(false);
        view.bindVouchersList(voucherListHasBeenFiltered);
        tookTheVoucherListForTheFirstTime = true;
    }

    @Override
    public void isMyVoucherEmpty() {
        view.isVouchersLoading(false);
        view.bindVouchersList(listOfAllOriginalVouchers);
    }

    @Override
    public void isVouchersListEmpty() {
        view.isVouchersLoading(false);
        tookTheVoucherListForTheFirstTime = true;
    }

    @Override
    public void isRedeemSuccess(boolean isSuccess) {

    }
}
