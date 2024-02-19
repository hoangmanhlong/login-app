package com.example.loginapp.view.fragments.product_detail;

public class NewProductInBasketMessage {
    private boolean hasNewProduct;

    public NewProductInBasketMessage(boolean hasNewProduct) {
        this.hasNewProduct = hasNewProduct;
    }

    public boolean isHasNewProduct() {
        return hasNewProduct;
    }
}
