package com.example.loginapp.presenter;

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

    public final List<FirebaseProduct> basket = new ArrayList<>();

    public List<FirebaseProduct> selectedProduct = new ArrayList<>();

    private final Order order = new Order();

    public CartPresenter(CartView view) {
        this.view = view;
    }

    public void initBasket() {
        if (basket.isEmpty()) interator.getCartProductsFromFirebase();
        else {
            view.bindProducts(basket);
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
    public void notifyItemAdded(FirebaseProduct product) {
        basket.add(product);
        updateUI();
        view.bindProducts(basket);
    }

    @Override
    public void notifyItemChanged(FirebaseProduct product) {
        int index = basket.indexOf(basket.stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).get(0));
        basket.set(index, product);
        updateUI();
        view.notifyItemChanged(index);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    @Override
    public void notifyItemRemoved(FirebaseProduct product) {
        int index = basket.indexOf(basket.stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).get(0));
        basket.remove(index);
        updateUI();
        view.notifyItemRemoved(index);
    }

    @Override
    public void isCartEmpty() {
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

    private void updateUI() {
        if (basket.isEmpty()) {
            view.isBasketEmpty(true);
            return;
        }

        view.isBasketEmpty(false);
        selectedProduct = basket.stream().filter(FirebaseProduct::isChecked).collect(Collectors.toList());
        if (!selectedProduct.isEmpty()) {
            order.setOrderProducts(toOrdersProductList(selectedProduct));

            double subtotal = selectedProduct.stream().mapToInt(product -> product.getPrice() * Integer.parseInt(product.getQuantity())).sum();
            double total = subtotal;

            Voucher voucher = order.getVoucher();
            if (voucher != null) total = subtotal - (subtotal * voucher.getDiscountPercentage() / 100);
            view.setTotal(String.valueOf(subtotal), String.valueOf(selectedProduct.size()), String.valueOf(total));
        }
        view.isCheckAllCheckboxChecked(selectedProduct.size() == basket.size());
        view.showCheckoutView(!selectedProduct.isEmpty());
        view.showCheckAllCheckbox(!basket.isEmpty());
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
