package com.example.loginapp.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAddresses implements Serializable {

    public final List<DeliveryAddress> deliveryAddresses;

    public DeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }
}
