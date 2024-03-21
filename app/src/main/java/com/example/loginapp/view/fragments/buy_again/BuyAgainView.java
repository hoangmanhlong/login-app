package com.example.loginapp.view.fragments.buy_again;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.PaymentMethod;

import java.util.List;

public interface BuyAgainView {

    void bindAddress(DeliveryAddress address);

    void bindOrderProducts(List<OrderProduct> products);

    void getSharedData();

    void bindPaymentMethod(PaymentMethod paymentMethod);

    void bindMerchandiseSubtotal(String merchandiseSubtotal);

    void bindShippingCost(String shippingCost);

    void bindVoucherCode(String voucherCode);

    void hasVoucher(boolean hasVoucher);

    void bindReducedPrice(String reducedPrice);

    void bindTotalPayment(String totalPayment);

    void onCheckoutSuccess(boolean success);

    void isLoading(boolean isLoading);

    void navigateSelectPaymentMethodFragment(PaymentMethod paymentMethod);
}
