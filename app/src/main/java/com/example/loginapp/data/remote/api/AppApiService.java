package com.example.loginapp.data.remote.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.example.loginapp.App;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppApiService {
    private static final String BASE_URL = "https://dummyjson.com";

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new ChuckerInterceptor(App.getInstance().getApplicationContext())).build();
    public static AppApi retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()
            .create(AppApi.class);
}
