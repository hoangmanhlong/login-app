package com.example.loginapp.view.fragments.buy_again;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.OrderProduct;

import java.util.List;

public interface BuyAgainView {

    void bindAddress(DeliveryAddress address);

    void bindOrderProducts(List<OrderProduct> products);
}
