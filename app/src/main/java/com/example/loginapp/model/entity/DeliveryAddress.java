package com.example.loginapp.model.entity;

import com.example.loginapp.utils.Constant;

import java.io.Serializable;

public class DeliveryAddress implements Serializable {

    private String deliveryAddressId = "DA" + System.currentTimeMillis();

    private String recipientName;

    private String phoneNumber;

    private String address;

    private String province;

    private String postalCode;

    private String country = Constant.VIETNAM;

    private String shippingOptions;

    private Boolean isDefault = false;

    public DeliveryAddress() {
    }

    public DeliveryAddress(String deliveryAddressId, String recipientName, String phoneNumber, String address, String province, String postalCode, String country, String shippingOptions, Boolean isDefault) {
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

    public String getPostalCode() {
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

    public void setDeliveryAddressId(String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setShippingOptions(String shippingOptions) {
        this.shippingOptions = shippingOptions;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public DeliveryAddress copy(DeliveryAddress deliveryAddress) {
        this.deliveryAddressId = deliveryAddress.deliveryAddressId;
        this.recipientName = deliveryAddress.recipientName;
        this.phoneNumber = deliveryAddress.phoneNumber;
        this.address = deliveryAddress.address;
        this.province = deliveryAddress.province;
        this.postalCode = deliveryAddress.postalCode;
        this.country = deliveryAddress.country;
        this.shippingOptions = deliveryAddress.shippingOptions;
        this.isDefault = deliveryAddress.isDefault;
        return this;
    }

    public static final String IS_DEFAULT = "isDefault";
}
