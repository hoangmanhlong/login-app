package com.example.loginapp.view.fragments.product_detail;

public class NewProductInWishlistMessage {
    private boolean hasNewProduct;

    public NewProductInWishlistMessage(boolean hasNewProduct) {
        this.hasNewProduct = hasNewProduct;
    }

    public boolean isHasNewProduct() {
        return hasNewProduct;
    }
}
