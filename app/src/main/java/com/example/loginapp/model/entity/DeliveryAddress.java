package com.example.loginapp.model.entity;

import java.io.Serializable;

public class DeliveryAddress implements Serializable {

    private String deliveryAddressId;

    private String recipientName;

    private String phoneNumber;

    private String address;

    private String province;

    private int postalCode;

    private String country;

    private String shippingOptions;

    private Boolean isDefault = false;

    public DeliveryAddress() {}

    public DeliveryAddress(String deliveryAddressId, String recipientName, String phoneNumber, String address, String province, int postalCode, String country, String shippingOptions, Boolean isDefault) {
        this.deliveryAddressId = deliveryAddressId;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.shippingOptions = shippingOptions;
        this.isDefault = isDefault;
    }

    public DeliveryAddress(String recipientName, String phoneNumber, String address, String province, int postalCode, String country, String shippingOptions) {
        this.deliveryAddressId = "DA" + System.currentTimeMillis();
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.shippingOptions = shippingOptions;
    }

    public DeliveryAddress(String recipientName, String phoneNumber, String address, String province, int postalCode, String country, String shippingOptions, Boolean isDefault) {
        this.deliveryAddressId = "DA" + System.currentTimeMillis();
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.shippingOptions = shippingOptions;
        this.isDefault = isDefault;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getShippingOptions() {
        return shippingOptions;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "deliveryAddressId='" + deliveryAddressId + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                ", shippingOptions='" + shippingOptions + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }

    public String getFormatPostalCode() {
        return String.valueOf(postalCode);
    }
}
