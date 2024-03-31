package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.FavoriteInteractor;
import com.example.loginapp.model.listener.FavoriteListener;
import com.example.loginapp.view.fragments.favorite_product.FavoriteView;

import java.util.ArrayList;
import java.util.List;


public class FavoritePresenter implements FavoriteListener {

    private FavoriteInteractor interactor;

    private FavoriteView view;

    public List<Product> wishlist;

    public FavoritePresenter(FavoriteView view) {
        this.view = view;
        interactor = new FavoriteInteractor(this);
        wishlist = new ArrayList<>();
    }

    /**
     * Wishlist đã được lấy từ backend hay chưa?
     * <p>
     * Default : false
     */
    private Boolean retrievedData = false;

    /**
     * Khởi tạo data khi màn hình được tạo giúp ứng dụng phản hồi ngay lập tức tới người dùng
     */
    public void initData() {
        if (retrievedData && view != null) {
            if (wishlist.isEmpty()) view.isWishlistEmpty(true);
            else {
                view.isWishlistEmpty(false);
                view.bindFavoriteListProduct(wishlist);
            }
        }
    }

    public void addFavoriteListValueEventListener() {
        interactor.addFavoriteListValueEventListener();
    }

    public void removeFavoriteListValueEventListener() {
        interactor.removeFavoriteListValueEventListener();
    }

    @Override
    public void isWishlistEmpty() {
        retrievedData = true;
        if (view != null) view.isWishlistEmpty(true);
    }

    @Override
    public void bindFavoriteListProduct(List<Product> products) {
        retrievedData = true;
        this.wishlist = products;
        if (view != null) {
            view.isWishlistEmpty(false);
            view.bindFavoriteListProduct(wishlist);
        }
    }

    public void deleteFavoriteProduct(int id) {
        interactor.deleteProduct(id);
    }

    public void detachView() {
        wishlist = null;
        view = null;
        retrievedData = null;
        interactor.clearData();
        interactor = null;
    }
}
