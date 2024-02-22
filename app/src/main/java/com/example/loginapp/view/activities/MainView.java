package com.example.loginapp.view.activities;

public interface MainView {

    void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty);

    void hasNewProductInFavoritesList(boolean hasNewProduct);

    void hasUser(boolean hasUser);
}
