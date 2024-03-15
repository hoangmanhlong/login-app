package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.CartInterator;
import com.example.loginapp.model.listener.CartListener;
import com.example.loginapp.view.fragments.cart.CartView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartPresenter implements CartListener {

    private final String TAG = this.toString();

    private final CartInterator interator = new CartInterator(this);

    private final CartView view;

    public List<FirebaseProduct> basket = new ArrayList<>();

    public List<FirebaseProduct> selectedProduct = new ArrayList<>();

    private boolean wasTakenForTheFirstTime = false;

    private final Order order = new Order();

    public CartPresenter(CartView view) {
        this.view = view;
    }

    public void initBasket() {
        if (wasTakenForTheFirstTime) {
            if (!basket.isEmpty()) {
                view.bindBasket(basket);
                updateUI();
            }
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setSelectedVoucher(Voucher selectedVoucher) {
        order.setVoucher(selectedVoucher);
        view.bindDiscountCode(selectedVoucher.getVoucherCode());
        view.clearDiscountCode(false);
        updateUI();
    }

    public void setClearCode(Boolean clear) {
        order.setVoucher(null);
        view.clearDiscountCode(clear);
        updateUI();
    }

    @Override
    public void getProductsFromShoppingCart(List<FirebaseProduct> products) {
        this.basket = products;
        wasTakenForTheFirstTime = true;
        view.bindBasket(basket);
        updateUI();
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }


    @Override
    public void isCartEmpty() {
        this.basket.clear();
        view.isCheckAllCheckboxChecked(false);
        view.isBasketEmpty(true);
    }

    public void deleteProductInFirebase(FirebaseProduct product) {
        interator.removeProductFromShoppingCart(product);
    }

    public void updateQuantity(int id, int quantity) {
        interator.updateQuantity(id, quantity);
    }

    public void onItemChecked(FirebaseProduct product, boolean checked) {
        if (checked) {
            interator.updateChecked(product.getId(), true);
        } else {
            view.setCheckAllChecked(false);
            interator.updateChecked(product.getId(), false);
        }
    }

    public void addShoppingCartValueEventListener() {
        interator.addShoppingCartValueEventListener();
    }

    public void removeShoppingCartValueEventListener() {
        interator.removeShoppingCartValueEventListener();
    }

    private void updateUI() {
        if (basket.isEmpty()) {
            view.isBasketEmpty(true);
            view.isCheckAllCheckboxChecked(false);
            return;
        }

        view.isBasketEmpty(false);
        selectedProduct = basket.stream().filter(FirebaseProduct::isChecked).collect(Collectors.toList());
        Log.d(TAG, "selectedProduct: " + selectedProduct.size());
        Log.d(TAG, "basket: " + basket.size());
        view.isCheckAllCheckboxChecked(selectedProduct.size() == basket.size());
        view.showCheckoutView(!selectedProduct.isEmpty());
        view.showCheckAllCheckbox(!basket.isEmpty());
        if (!selectedProduct.isEmpty()) {
            order.setOrderProducts(toOrdersProductList(selectedProduct));

            double subtotal = selectedProduct.stream().mapToInt(product -> product.getPrice() * Integer.parseInt(product.getQuantity())).sum();
            double total = subtotal;

            Voucher voucher = order.getVoucher();
            if (voucher != null) total = subtotal - (subtotal * voucher.getDiscountPercentage() / 100);
            view.setTotal(String.valueOf(subtotal), String.valueOf(selectedProduct.size()), String.valueOf(total));
        }
    }

    public void updateCheckboxAllSelected(Boolean checked) {
        for (FirebaseProduct product : basket) interator.updateChecked(product.getId(), checked);
    }

    private List<OrderProduct> toOrdersProductList(List<FirebaseProduct> firebaseProducts) {
        List<OrderProduct> list = new ArrayList<>();
        for (FirebaseProduct product : firebaseProducts) list.add(product.toOrderProduct());
        return list;
    }
}
