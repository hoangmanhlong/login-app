package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Coin;
import com.example.loginapp.model.interator.CoinsRewardInterator;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.view.fragment.coins.CoinsRewardView;

import java.util.Calendar;
import java.util.List;

public class CoinsRewardPresenter implements CoinsRewardListener {

    private final CoinsRewardInterator interator = new CoinsRewardInterator(this);

    private final CoinsRewardView view;

    private boolean isCurrentDatePresent;

    public CoinsRewardPresenter(CoinsRewardView view) {
        this.view = view;
    }

    @Override
    public void getCalendar(Coin coin) {
        Log.d(this.toString(), "getCalendar: " + coin.getEndDate());
        view.setCoin(coin);
        List<Long> dates = coin.getRollCalllist();
        long currentDate = System.currentTimeMillis();
        isCurrentDatePresent = dates.stream().anyMatch(date -> isSameDay(date, currentDate));
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
}
