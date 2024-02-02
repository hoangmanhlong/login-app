package com.example.loginapp.data.remote.api;


import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.data.remote.api.dto.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppApi {
    @GET("products?limit=0")
    Call<ProductResponse> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int productId);

    @GET("products/categories")
    Call<List<String>> getCategories();

    @GET("products/category/{categoryName}")
    Call<ProductResponse> getProductOfCategory(@Path("categoryName") String category);

    @GET("products/search")
    Call<ProductResponse> searchProduct(@Query("q") String query);

    @GET("comments")
    Call<CommentRespond> getComments(@Query("limit") int limit);

}
