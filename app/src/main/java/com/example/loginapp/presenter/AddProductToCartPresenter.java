package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.AddProductToCartInteractor;
import com.example.loginapp.model.listener.AddProductToCartListener;
import com.example.loginapp.view.fragments.add_to_cart.IAddProductToCartView;

public class AddProductToCartPresenter implements AddProductToCartListener {

    private AddProductToCartInteractor interactor;

    private Product product;

    private int quantity = 1;

    private IAddProductToCartView view;

    public AddProductToCartPresenter(IAddProductToCartView view) {
        this.view = view;
        interactor = new AddProductToCartInteractor(this);
    }

    public void clear() {
        interactor.clear();
        interactor = null;
        product = null;
        view = null;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        bindQuantity();
    }

    public void setProduct(Product product) {
        this.product = product;
        if (view != null) view.bindProduct(product);
        bindQuantity();
    }

    public void increasingTheNumber() {
        quantity++;
        bindQuantity();
    }

    public void reduceTheNumberOf() {
        if (quantity > 1) {
            quantity--;
            bindQuantity();
        }
    }

    public void bindQuantity() {
        if (view != null) view.bindQuantity(String.valueOf(quantity));
    }

    public void addProductToCart() {
        FirebaseProduct firebaseProduct = product.toProductInCart(quantity);
        interactor.addProductToCart(firebaseProduct);
    }

    @Override
    public void isAddProductSuccess(boolean success) {
        if (success) view.dismissFragment();
    }
}
