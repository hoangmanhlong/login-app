package com.example.loginapp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.CartInteractor;
import com.example.loginapp.model.listener.CartListener;
import com.example.loginapp.view.fragments.cart.CartView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CartPresenter implements CartListener {

    private final String TAG = CartPresenter.class.getSimpleName();

    private CartInteractor interactor;

    private CartView view;

    public List<FirebaseProduct> basket;

    public List<FirebaseProduct> selectedProduct;

    private Boolean retrievedData = false;

    private Order order;

    private ExecutorService executorService;

    private Handler handler;

    public CartPresenter(CartView view) {
        this.view = view;
        interactor = new CartInteractor(this);
        basket = new ArrayList<>();
        selectedProduct = new ArrayList<>();
        order = new Order();
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void initBasket() {
        if (retrievedData) {
            if (!basket.isEmpty()) {
                view.bindBasket(basket);
            }
            updateUI();
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
        retrievedData = true;
        view.bindBasket(basket);
        updateUI();
    }

    @Override
    public void isCartEmpty() {
        retrievedData = true;
        basket.clear();
        updateUI();
    }

    public void deleteProductInFirebase(int productId) {
        interactor.removeProductFromShoppingCart(productId);
    }

    public void updateQuantity(int id, int quantity) {
        interactor.updateQuantity(id, quantity);
    }

    public void onItemChecked(FirebaseProduct product) {
        executorService.execute(() -> interactor.updateChecked(product.getId(), !product.isChecked()));
    }

    public void addShoppingCartValueEventListener() {
        interactor.addShoppingCartValueEventListener();
    }

    public void removeShoppingCartValueEventListener() {
        interactor.removeShoppingCartValueEventListener();
    }

    private void updateUI() {
        if (basket.isEmpty()) {
            view.isCheckAllCheckboxChecked(false);
            view.isBasketEmpty(true);
            return;
        }

        view.isBasketEmpty(false);
        selectedProduct = basket.stream().filter(FirebaseProduct::isChecked).collect(Collectors.toList());
        view.isCheckAllCheckboxChecked(selectedProduct.size() == basket.size());
        view.showCheckoutView(!selectedProduct.isEmpty());
        view.showCheckAllCheckbox(!basket.isEmpty());
        if (!selectedProduct.isEmpty()) {
            executorService.execute(() -> {
                order.setOrderProducts(toOrdersProductList(selectedProduct));

                double subtotal = selectedProduct.stream().mapToInt(product -> product.getPrice() * product.getQuantity()).sum();
                double total = subtotal;

                Voucher voucher = order.getVoucher();
                if (voucher != null)
                    total = subtotal - (subtotal * voucher.getDiscountPercentage() / 100);
                double finalTotal = total;
                handler.post(() ->
                        view.setTotal(String.valueOf(subtotal), String.valueOf(selectedProduct.size()), String.valueOf(finalTotal))
                );

            });

        }
    }

    public void updateCheckboxAllSelected(Boolean checked) {
        executorService.execute(() -> {
            for (FirebaseProduct product : basket) interactor.updateChecked(product.getId(), checked);
        });
    }

    private List<OrderProduct> toOrdersProductList(List<FirebaseProduct> firebaseProducts) {
        List<OrderProduct> list = new ArrayList<>();
        for (FirebaseProduct product : firebaseProducts) list.add(product.toOrderProduct());
        return list;
    }

    public void detachView() {
        view = null;
        basket = null;
        selectedProduct = null;
        order = null;
        retrievedData = null;
        interactor.clearData();
        interactor = null;
        executorService.shutdown();
        executorService = null;
        handler = null;
    }
}
