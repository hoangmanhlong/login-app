package com.example.loginapp.model.entity;

import java.io.Serializable;

public class Voucher implements Serializable {

    private String voucherCode = "VC" + System.currentTimeMillis();

    private String voucherDescription;

    private float discountPercentage;

    private Long startDate;

    private Long endDate;

    private int quantityLimit;

    private Long minPriceApply;

    private Long minimizePrice;

    private VoucherStatus voucherStatus;

    private PaymentMethod paymentMethod;

    private VoucherType voucherType;

    public Voucher() {
    }

    public Voucher(String voucherDescription, float discountPercentage, Long startDate, Long endDate, int quantityLimit, Long minPriceApply, Long minimizePrice, VoucherStatus voucherStatus, PaymentMethod paymentMethod, VoucherType voucherType) {
        this.voucherDescription = voucherDescription;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantityLimit = quantityLimit;
        this.minPriceApply = minPriceApply;
        this.minimizePrice = minimizePrice;
        this.voucherStatus = voucherStatus;
        this.paymentMethod = paymentMethod;
        this.voucherType = voucherType;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public int getQuantityLimit() {
        return quantityLimit;
    }

    public Long getMinPriceApply() {
        return minPriceApply;
    }

    public Long getMinimizePrice() {
        return minimizePrice;
    }

    public VoucherStatus getVoucherStatus() {
        return voucherStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "voucherCode='" + voucherCode + '\'' +
                ", voucherDescription='" + voucherDescription + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", quantityLimit=" + quantityLimit +
                ", minPriceApply=" + minPriceApply +
                ", minimizePrice=" + minimizePrice +
                ", voucherStatus=" + voucherStatus +
                ", paymentMethod=" + paymentMethod +
                ", voucherType=" + voucherType +
                '}';
    }
}