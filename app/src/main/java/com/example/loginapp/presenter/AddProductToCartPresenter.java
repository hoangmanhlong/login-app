package com.example.loginapp.presenter;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.AddProductToCartInteractor;
import com.example.loginapp.model.listener.AddProductToCartListener;
import com.example.loginapp.view.fragments.add_to_cart.AddProductToCartView;

public class AddProductToCartPresenter implements AddProductToCartListener {

    private final AddProductToCartInteractor interactor = new AddProductToCartInteractor(this);

    private Product product;

    private int quantity = 1;

    private final AddProductToCartView view;

    public AddProductToCartPresenter(AddProductToCartView view) {
        this.view = view;
    }

    public void initData() {
        bindQuantity();
        view.getSharedData();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        bindQuantity();
    }

    public void setProduct(Product product) {
        this.product = product;
        view.bindProduct(product);
    }

    public void increasingTheNumber() {
        quantity++;
        bindQuantity();
    }

    public void reduceTheNumberOf() {
        quantity--;
        bindQuantity();
    }

    public void bindQuantity() {
        view.bindQuantity(String.valueOf(quantity));
    }

    public void addProductToCart() {
        FirebaseProduct firebaseProduct = product.toProductInCart(quantity);
        interactor.addProductToCart(firebaseProduct);
    }

    @Override
    public void isAddProductSuccess(boolean success) {
        if (success) {
            view.dismissAddProductToCartFragment();
            view.onMessage(R.string.added_to_cart);
        }
    }
}
