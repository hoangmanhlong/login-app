package com.example.loginapp.data.local.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.ProductName;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(List<ProductName> products);

    @Query("SELECT * FROM ProductName")
    Observable<List<ProductName>> getProductsName();

}
