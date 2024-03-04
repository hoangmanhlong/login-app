package com.example.loginapp.model.entity;

public class DayWithCheck {

    private int day;

    private boolean isChecked = false;

    public DayWithCheck(int day, boolean isChecked) {
        this.day = day;
        this.isChecked = isChecked;
    }

    public DayWithCheck(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DayWithCheck{" +
                "day=" + day +
                ", isChecked=" + isChecked +
                '}';
    }
}
