package com.example.loginapp.presenter;

import android.os.Build;
import android.util.Log;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DayWithCheck;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.CoinsRewardInteractor;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.view.fragments.coins.CoinsRewardView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Presenter for Coins Reward Screen - {@link CoinsRewardPresenter}.
 * @author hoangmanhlong
 */
public class CoinsRewardPresenter implements CoinsRewardListener {

    private static final String TAG = CoinsRewardPresenter.class.getSimpleName();

    /**
     * Dữ liệu về điểm danh đã được lấy chưa?
     * @Default false
     */
    private boolean wasCoinsTakenForTheFirstTime = false;

    /**
     * Danh sách voucher đã được lấy chưa?
     * @Default false
     */
    private boolean tookTheVoucherListForTheFirstTime = false;

    /**
     * Declare a {@link CoinsRewardInteractor}
     */
    private CoinsRewardInteractor interactor;

    /**
     * Declare object holder data attendance {@link AttendanceManager}
     */
    private AttendanceManager attendanceManager;

    /**
     * Danh sách các ngày trong tuần hiện tại
     */
    private List<DayWithCheck> daysInWeek = new ArrayList<>();

    /**
     * Danh sách các ngày đã điểm danh
     */
    private List<DayWithCheck> checkedDays = new ArrayList<>();

    private CoinsRewardView view;

    /**
     * Ngày cuối cùng trong tháng hiện tại
     */
    private Date lastDayOfMonth;

    /**
     * Hôm này đã điểm danh hay chưa?
     * @Default false
     */
    private boolean tookAttendance = false;

    /**
     * Khai báo danh sách tất cả các phiếu giảm giá mà người dùng có thể đối.
     * Danh sách này được lấy từ Server. Tất cả người dùng điều nhận được danh sách này.
     */
    private List<Voucher> listOfAllOriginalVouchers;

    /**
     * Danh sách voucher đã được lọc và hiển thị trên màn hình
     * Mục đính: Người dùng chỉ được đổi những voucher mà người dùng chưa có - tránh đổi cùng
     * 1 Voucher.
     * <p>
     * Danh sách được này =
     * tất cả voucher từ server - danh sách voucher của người dùng
     */
    private List<Voucher> voucherListHasBeenFiltered;

    /**
     * Ngày hiện tại - lấy từ hệ thống Android
     */
    private int currentDay;

    public CoinsRewardPresenter(CoinsRewardView view) {
        this.view = view;
        interactor = new CoinsRewardInteractor(this);
        listOfAllOriginalVouchers = new ArrayList<>();
        voucherListHasBeenFiltered = new ArrayList<>();
        getsTheCurrentWeekday();
    }

    private void getsTheCurrentWeekday() {
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

    /**
     * Xóa toàn bộ dữ liệu
     * khi {@link com.example.loginapp.view.fragments.coins.CoinsRewardFragment} bị hủy.
     * <p>
     *Mục đính: Giải phóng bộ nhớ.
     */
    public void clear() {
        view = null;
        interactor = null;
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

    /**
     * This method called when "Redeem Button" clicked
     * @param voucher Selected voucher by user.
     * Kiểm tra xem nếu điểm hiện tại có lớn hơn hoặc bằng điểm có thể đổi voucher hay không?
     * Nếu đúng thực hiện đổi ngược lại hiển thị thông báo
     */
    public void redeemVoucher(Voucher voucher) {
        int numberOfCoinsNeededToExchange = voucher.getNumberOfCoinsNeededToExchange();
        if (attendanceManager != null && attendanceManager.getNumberOfCoins() >= numberOfCoinsNeededToExchange) {
            interactor.redeemVoucher(
                    voucher,
                    attendanceManager.getNumberOfCoins() - numberOfCoinsNeededToExchange
            );
        } else {
            view.showSnackBar(R.string.can_not_redeem_message);
        }
    }

    public void addAttendanceDataValueEventListener() {
        view.isAttendanceLoading(true); // show view holder when loading
        interactor.addAttendanceDataValueEventListener(); // add a listener
    }

    public void removeAttendanceDataValueEventListener() {
        interactor.removeAttendanceDataValueEventListener();
    }

    public void addVouchersValueEventListener() {
        view.isVouchersLoading(true); // show view holder when loading
        interactor.addVouchersValueEventListener();
    }

    public void removeVouchersValueEventListener() {
        interactor.removeVouchersValueEventListener();
    }

    public void addMyVouchersValueEventListener() {
        interactor.addMyVouchersValueEventListener();
    }

    public void removeMyVouchersValueEventListener() {
        interactor.removeMyVouchersValueEventListener();
    }

    /**
     * This method called when "Get 100 coins now" button Clicked
     */
    public void attendance() {
        if (!tookAttendance) {
            List<Integer> newCheckedDays = new ArrayList<>();

            if (attendanceManager != null) newCheckedDays = attendanceManager.getCheckedDates();

            newCheckedDays.add(currentDay);

            interactor.attendance(
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

    /**
     * This method called when data from server return
     * @param vouchers Danh sách tất cả voucher từ Hệ thống ứng dụng
     */
    @Override
    public void getVouchers(List<Voucher> vouchers) {
        this.listOfAllOriginalVouchers = vouchers;
        addMyVouchersValueEventListener(); // Lấy danh sách voucher của người dùng
    }

    /**
     * Phương thức này nhận 1 dạnh sách voucher của người dùng so sánh với voucher của hệ thống
     * Nếu voucher của người dùng có trong hệ thống voucher của server thì xóa nó
     * <p>
     * tookTheVoucherListForTheFirstTime = true
     * @param vouchers Danh sách Voucher của người dùng
     */
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
        voucherListHasBeenFiltered = listOfAllOriginalVouchers;
        view.isVouchersLoading(false);
        view.bindVouchersList(voucherListHasBeenFiltered);
        tookTheVoucherListForTheFirstTime = true;
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
