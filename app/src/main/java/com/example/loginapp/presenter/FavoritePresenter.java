package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.FavoriteInterator;
import com.example.loginapp.model.listener.FavoriteListener;
import com.example.loginapp.view.fragments.favorite_product.FavoriteView;

import java.util.ArrayList;
import java.util.List;

public class FavoritePresenter implements FavoriteListener {

    private FavoriteInterator interator;

    private FavoriteView view;

    public List<Product> wishlist;

    public FavoritePresenter(FavoriteView view) {
        this.view = view;
        interator = new FavoriteInterator(this);
        wishlist = new ArrayList<>();
    }

    // Whether the list has been retrieved from the backend or not - Danh sách đã được lấy từ backend hay chưa
    private boolean isChecked = false;

    public void initData() {
        /**
         * @Vietnamese Danh sách đã được lấy từ backend và không rỗng THÌ tải lên VIEW - Điều này giúp VIEW
         * hiển thị dữ liệu với người dùng ngay.
         * @english: The list has been retrieved from the backend and is not empty THEN upload
         * to the VIEW - This helps the VIEW display the data to the user immediately.
         */
        if (!wishlist.isEmpty() && isChecked) view.bindFavoriteListProduct(wishlist);
        if (wishlist.isEmpty() && isChecked) view.isWishlistEmpty(true);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    public void addFavoriteListValueEventListener() {
        interator.addFavoriteListValueEventListener();
    }

    public void removeFavoriteListValueEventListener() {
        interator.removeFavoriteListValueEventListener();
    }

    @Override
    public void isWishlistEmpty() {
        isChecked = true;
        view.isWishlistEmpty(true);
    }

    @Override
    public void bindFavoriteListProduct(List<Product> products) {
        isChecked = true;
        this.wishlist = products;
        view.bindFavoriteListProduct(wishlist);
        view.isWishlistEmpty(false);
    }

    public void deleteFavoriteProduct(int id) {
        interator.deleteProduct(id);
    }

    public void detachView() {
        wishlist = null;
        view = null;
        interator.clearData();
        interator = null;
    }
}
