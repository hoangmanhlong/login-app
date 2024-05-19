package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.CartListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartInteractor {

//    private final String TAG = this.toString();

    private DatabaseReference cartRef = Constant.cartRef;

    private String uid;

    private CartListener listener;

    private ValueEventListener shoppingCartValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<FirebaseProduct> products = new ArrayList<>();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        products.add(dataSnapshot.getValue(FirebaseProduct.class));
                    listener.getProductsFromShoppingCart(products);
                } else {
                    listener.isCartEmpty();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public CartInteractor(CartListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void addShoppingCartValueEventListener() {
        if (uid != null){
            Constant.cartRef.child(uid).addValueEventListener(shoppingCartValueEventListener);
        }
    }

    public void removeShoppingCartValueEventListener() {
        if (uid != null) {
            Constant.cartRef.child(uid).removeEventListener(shoppingCartValueEventListener);
        }
    }

    public void updateQuantity(int id, int quantity) {
        if (uid != null) {
            cartRef.child(uid).child(String.valueOf(id)).child("quantity").setValue(quantity);
        }
    }

    public void updateChecked(int id, boolean checked) {
        if (uid != null) {
            cartRef.child(uid).child(String.valueOf(id)).child("checked").setValue(checked);
        }
    }

    public void removeProductFromShoppingCart(int productId) {
        if (uid != null) {
            cartRef.child(uid).child(String.valueOf(productId)).removeValue();
        }
    }

    public void clearData() {
        listener = null;
        cartRef = null;
        uid = null;
        shoppingCartValueEventListener = null;
    }
}
