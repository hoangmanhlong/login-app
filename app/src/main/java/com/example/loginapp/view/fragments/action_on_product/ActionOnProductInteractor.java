package com.example.loginapp.view.fragments.action_on_product;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;

public class ActionOnProductInteractor {

    private DatabaseReference favoriteProductRef = Constant.favoriteProductRef;

    private String uid;

    private ActionOnProductListener listener;

    public ActionOnProductInteractor(ActionOnProductListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void clear() {
        uid = null;

    }

    public void saveFavoriteProduct(Product product) {
        String productId = String.valueOf(product.getId());
        favoriteProductRef.child(uid).child(productId).setValue(product)
                .addOnCompleteListener(s -> {
                    EventBus.getDefault().postSticky(new NewProductInWishlistMessage());
                    if (listener != null) listener.addToWishlistSuccess();
                });
    }
}
