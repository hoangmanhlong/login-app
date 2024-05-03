package com.example.loginapp.model.listener;

import com.example.loginapp.data.remote.api.dto.CommentDto;
import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface ProductListener {

    void isFavoriteProduct(Boolean isFavoriteProduct);

    void getCommentRespond(CommentDto commentDto);

    void getSimilarProducts(List<Product> products);
}
