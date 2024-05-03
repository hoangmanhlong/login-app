package com.example.loginapp.data.remote.api;


import com.example.loginapp.data.remote.api.dto.CommentDto;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.data.remote.api.dto.ProductDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppApi {
    @GET("products?limit=0")
    Call<ProductDto> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int productId);

    @GET("products/categories")
    Call<List<String>> getCategories();

    @GET("products/category/{categoryName}")
    Call<ProductDto> getProductOfCategory(@Path("categoryName") String category);

    @GET("products/search")
    Call<ProductDto> searchProduct(@Query("q") String query);

    @GET("comments")
    Call<CommentDto> getComments(@Query("limit") int limit);

}
