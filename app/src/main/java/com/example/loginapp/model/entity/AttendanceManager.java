package com.example.loginapp.model.entity;

import java.util.List;

public class AttendanceManager {

    private List<Integer> checkedDates;

    private int numberOfCoins = 0;

    private Date expiratioDate;

    public AttendanceManager() {}

    public AttendanceManager(List<Integer> checkedDates, int numberOfCoins) {
        this.checkedDates = checkedDates;
        this.numberOfCoins = numberOfCoins;
    }

    public AttendanceManager(List<Integer> checkedDates, int numberOfCoins, Date expiratioDate) {
        this.checkedDates = checkedDates;
        this.numberOfCoins = numberOfCoins;
        this.expiratioDate = expiratioDate;
    }

    public List<Integer> getCheckedDates() {
        return checkedDates;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public Date getExpiratioDate() {
        return expiratioDate;
    }

    public static String NUMBER_OF_COINS = "numberOfCoins";

    public static String CHECKED_DATES = "checkedDates";
}
