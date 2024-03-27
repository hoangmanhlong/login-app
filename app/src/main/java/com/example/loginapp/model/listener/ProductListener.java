package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface ProductListener {

    void isFavoriteProduct(Boolean isFavoriteProduct);

    void saveToBasketSuccess();

    void getCommentRespond(CommentRespond commentRespond);

    void getSimilarProducts(List<Product> products);
}
