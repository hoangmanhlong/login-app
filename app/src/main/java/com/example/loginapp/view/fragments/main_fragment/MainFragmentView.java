package com.example.loginapp.view.fragments.main_fragment;

public interface MainFragmentView {

    void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty);

    void hasNewProductInFavoritesList(boolean hasNewProduct);
}
