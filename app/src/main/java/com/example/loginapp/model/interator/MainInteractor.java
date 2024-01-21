package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.MainListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainInteractor {
    private final DatabaseReference cartRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.CART_REF);

    private final DatabaseReference favoriteRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.FAVORITE_PRODUCT_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final MainListener listener;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
    }

    public void getNumber() {
        if (currentUser != null) {
            String id = currentUser.getUid();
            cartRef.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = 0;
                    List<FirebaseProduct> products = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        FirebaseProduct product = postSnapshot.getValue(FirebaseProduct.class);
//                        products.add(product);
                        count++;
                    }
                    listener.onLoadBasket(count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            favoriteRef.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = 0;
                    List<Product> products = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        Product product = postSnapshot.getValue(Product.class);
//                        products.add(product);
                        count++;
                    }
                    listener.onLoadFavoriteProducts(count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
