package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.CartListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartInterator {

//    private final String TAG = this.toString();

    private final DatabaseReference cartRef = Constant.cartRef;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final CartListener listener;

    private final ValueEventListener shoppingCartValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                List<FirebaseProduct> products = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                    products.add(dataSnapshot.getValue(FirebaseProduct.class));
                listener.getProductsFromShoppingCart(products);
            } else {
                listener.isCartEmpty();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public CartInterator(CartListener listener) {
        this.listener = listener;
    }

    public void addShoppingCartValueEventListener() {
        Constant.cartRef.child(user.getUid()).addValueEventListener(shoppingCartValueEventListener);
    }

    public void removeShoppingCartValueEventListener() {
        Constant.cartRef.child(user.getUid()).removeEventListener(shoppingCartValueEventListener);
    }

    public void updateQuantity(int id, int quantity) {
        cartRef.child(user.getUid()).child(String.valueOf(id)).child("quantity").setValue(quantity);
    }

    public void updateChecked(int id, boolean checked) {
        cartRef.child(user.getUid()).child(String.valueOf(id)).child("checked").setValue(checked);
    }

    public void removeProductFromShoppingCart(FirebaseProduct product) {
        cartRef.child(user.getUid()).child(String.valueOf(product.getId())).removeValue();
    }
}
