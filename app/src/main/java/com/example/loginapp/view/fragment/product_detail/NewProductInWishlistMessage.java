package com.example.loginapp.view.fragment.product_detail;

public class NewProductInWishlistMessage {
    private boolean hasNewProduct;

    public NewProductInWishlistMessage(boolean hasNewProduct) {
        this.hasNewProduct = hasNewProduct;
    }

    public boolean isHasNewProduct() {
        return hasNewProduct;
    }
}
