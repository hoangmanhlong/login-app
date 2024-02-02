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
}


