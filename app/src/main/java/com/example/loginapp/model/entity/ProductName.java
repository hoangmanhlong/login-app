package com.example.loginapp.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductName {
    @NonNull
    @PrimaryKey
    public String productName;

    public ProductName(@NonNull String productName) {
        this.productName = productName;
    }
}
