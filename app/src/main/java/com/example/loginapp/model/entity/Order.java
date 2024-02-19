package com.example.loginapp.model.entity;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private String orderId;

    private List<OrderProduct> orderProducts;

    private Long orderDate;

    private OrderStatus orderStatus;

    private DeliveryAddress deliveryAddress;

    private PaymentMethod paymentMethod;

    private int paymentTotal;

    private Voucher voucher;

    public Order() {}

    public Order(List<OrderProduct> orderProducts, DeliveryAddress deliveryAddress, PaymentMethod paymentMethod, int paymentTotal) {
        this.orderId =  "SA" + System.currentTimeMillis();
        this.orderProducts = orderProducts;
        this.orderDate = System.currentTimeMillis();
        this.orderStatus = OrderStatus.Processing;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paymentTotal = paymentTotal;
    }

    public Order(List<OrderProduct> orderProducts, DeliveryAddress deliveryAddress, PaymentMethod paymentMethod, int paymentTotal, Voucher voucher) {
        this.orderId =  "SA" + System.currentTimeMillis();
        this.orderProducts = orderProducts;
        this.orderDate = System.currentTimeMillis();
        this.orderStatus = OrderStatus.Processing;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paymentTotal = paymentTotal;
        this.voucher = voucher;
    }

    public Order(List<OrderProduct> orderProducts, DeliveryAddress deliveryAddress) {
        this.orderProducts = orderProducts;
        this.deliveryAddress = deliveryAddress;
    }

    public Order(List<OrderProduct> orderProducts, DeliveryAddress deliveryAddress, Voucher voucher) {
        this.orderProducts = orderProducts;
        this.deliveryAddress = deliveryAddress;
        this.voucher = voucher;
    }

    public Order(List<OrderProduct> orderProducts, Voucher voucher) {
        this.orderProducts = orderProducts;
        this.voucher = voucher;
    }

    public Order(List<OrderProduct> products) {
        this.orderProducts = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public int getPaymentTotal() {
        return paymentTotal;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentTotal(int paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
}


