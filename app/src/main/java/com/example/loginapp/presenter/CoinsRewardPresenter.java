package com.example.loginapp.presenter;

import com.example.loginapp.model.interator.CoinsRewardInterator;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.view.fragments.coins.CoinsRewardView;

import java.util.Calendar;

public class CoinsRewardPresenter implements CoinsRewardListener {

    private final CoinsRewardInterator interator = new CoinsRewardInterator(this);

    private final CoinsRewardView view;

    public CoinsRewardPresenter(CoinsRewardView view) {
        this.view = view;
    }

    public void initData() {
        getLastDayOfMonth();
    }

    public void getCalendar() {
        interator.getCalendar();
    }

    public void attendance() {
        interator.attendance();
    }

    private boolean isSameDay(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(timestamp2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();

        // Thiết lập ngày là ngày cuối cùng của tháng hiện tại
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Lấy ngày cuối cùng của tháng hiện tại
        int lastDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
        int year = calendar.get(Calendar.YEAR);
        view.getLastDayOfMonth(lastDayOfMonth + "-" + month + "-" + year);
    }
}
