package com.example.loginapp.view.fragment.product_detail;

public class NewProductInBasketMessage {
    private boolean hasNewProduct;

    public NewProductInBasketMessage(boolean hasNewProduct) {
        this.hasNewProduct = hasNewProduct;
    }

    public boolean isHasNewProduct() {
        return hasNewProduct;
    }
}
