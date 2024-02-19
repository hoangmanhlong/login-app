package com.example.loginapp.model.entity;

import java.util.Calendar;
import java.util.List;

public class Coin {

    private List<Long> rollCalllist;

    private int numberOfCoins = 0;

    private Long endDate;

    public Coin() {}

    public Coin(List<Long> rollCalllist, int numberOfCoins) {
        this.rollCalllist = rollCalllist;
        this.numberOfCoins = numberOfCoins;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.endDate = calendar.getTimeInMillis();
    }

    public List<Long> getRollCalllist() {
        return rollCalllist;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public Long getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "rollCalllist=" + rollCalllist +
                ", numberOfCoins=" + numberOfCoins +
                ", endDate=" + endDate +
                '}';
    }
}
