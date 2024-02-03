package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.CommentRespond;

public interface ProductListener {

    void onMessage(String message);

    void isFavoriteProduct(Boolean isFavoriteProduct);

    void saveToBasketSuccess();

    void getCommentRespond(CommentRespond commentRespond);

    void hasNewFavoriteProduct();
}
