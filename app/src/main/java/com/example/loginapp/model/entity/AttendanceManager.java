package com.example.loginapp.model.entity;

import java.util.List;

public class AttendanceManager {

    private List<Integer> checkedDates;

    private int numberOfCoins = 0;

    public List<Integer> getCheckedDates() {
        return checkedDates;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }
}
