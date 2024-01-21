package com.example.loginapp.model.entity;

import java.util.List;

public class Voucher {
    private String voucherCode;

    private String voucherName;

    private String voucherDescription;

    private float discountPercentage;

    private String validUntil;

    private String paymentMethods;

    private Long minPriceApply;

    private Long minimizePrice;

    private List<Category> categoriesApply;

    private String status;

    public Voucher() {}

    public Voucher(String voucherCode, String voucherName, String voucherDescription, float discountPercentage, String validUntil, String paymentMethods, String status) {
        this.voucherCode = voucherCode;
        this.voucherName = voucherName;
        this.voucherDescription = voucherDescription;
        this.discountPercentage = discountPercentage;
        this.validUntil = validUntil;
        this.paymentMethods = paymentMethods;
        this.status = status;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public String getStatus() {
        return status;
    }
}
