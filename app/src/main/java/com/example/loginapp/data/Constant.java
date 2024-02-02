package com.example.loginapp.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class Constant {

    public static final String EXPAND_LABEL_KEY = "expand_label";

    public static final String EXPAND_PRODUCTS_KEY = "expand";

    public static final String PHONE_NUMBER_KEY = "phone_number";

    public static final String EMAIL_KEY = "email";

    public static final String PASSWORD_KEY = "password";

    public static final String DELIVERY_ADDRESS_KEY = "delivery_address_key";

    public static final String USER_REF_NAME = "users";

    public static final String IMAGE_STORAGE_URL = "avatar_user_image";

    public static final String FAVORITE_PRODUCT_REF_KEY = "favorite_product";

    public static final String SEARCH_HISTORY_REF_KEY = "search_history";

    public static final String CART_REF_KEY = "cart";

    public static final String ORDER_KEY = "order";

    public static final String VOUCHER_KEY_NAME = "voucher";

    public static final String VOUCHER_REF_KEY = "vouchers";

    public static final String PRODUCT_KEY = "products";

    public static final String IS_CART = "is_cart";

    public static final String COINS_REF_NAME = "coins";

    public static final String SAVE_ADDRESS_KEY = "save_address";

    public static final String DELIVERY_ADDRESS_REF_NAME = "delivery_address";

    public static final String ORDER_REF_NAME = "orders";

    public static final String USER_KEY_NAME = "userdata";

    public static final int COINS_FOR_EACH_TIME = 100;


    public static final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public static final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(USER_REF_NAME);

    public static final DatabaseReference favoriteProductRef = FirebaseDatabase.getInstance().getReference().child(FAVORITE_PRODUCT_REF_KEY);

    public static final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child(CART_REF_KEY);

    public static final DatabaseReference deliveryAddressRef = FirebaseDatabase.getInstance().getReference().child(DELIVERY_ADDRESS_REF_NAME);

    public static final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child(ORDER_REF_NAME);

    public static final DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child(VOUCHER_REF_KEY);

    public static final DatabaseReference coinsRef = FirebaseDatabase.getInstance().getReference().child(COINS_REF_NAME);
}
