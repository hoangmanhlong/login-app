package com.example.loginapp.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class Constant {

    public static final int BACK_PRESS_INTERVAL = 2000;

    public static final String VIEWED_STATUS_KEY = "viewed_status";

    public static final String IS_VIEWED_FAVORITES_LIST_KEY = "isViewedFavoritesList";

    public static final String EXPAND_LABEL_KEY = "expand_label";

    public static final String EXPAND_PRODUCTS_KEY = "expand";

    public static final String PHONE_NUMBER_KEY = "phone_number";

    public static final String EMAIL_KEY = "email";

    public static final String PASSWORD_KEY = "password";

    public static final String DELIVERY_ADDRESS_KEY = "delivery_address_key";

    private static final String USER_REF_NAME = "users";

    public static final String IMAGE_STORAGE_URL = "avatar_user_image";

    private static final String FAVORITE_PRODUCT_REF_KEY = "favorite_product";

    public static final String PAYMENT_METHOD_KEY = "payment_method";

    public static final String SEARCH_HISTORY_REF_KEY = "search_history";

    private static final String CART_REF_KEY = "cart";

    public static final String DELIVERY_ADDRESSES_KEY = "delivery_addresses";

    public static final String ORDER_KEY = "order";

    public static final String VOUCHER_KEY_NAME = "voucher";

    private static final String VOUCHER_REF_KEY = "vouchers";

    public static final String PRODUCT_KEY = "products";

    public static final String IS_PRODUCTS_FROM_CART = "is_cart";

    private static final String COINS_REF_NAME = "coins";

    public static final String SAVE_ADDRESS_KEY = "save_address";

    private static final String DELIVERY_ADDRESS_REF_NAME = "delivery_address";

    public static final String ORDER_REF_NAME = "orders";

    public static final String USER_KEY_NAME = "userdata";

    public static final String BESTSELLER_REF_NAME = "bestseller";

    public static final String LOGIN_TAG = "login_tag";

    public static final String MY_VOUCHER_REF_NAME = "my_voucher";

    public static final int ShippingCost = 200;

    public static final DatabaseReference userRef =
            FirebaseDatabase.getInstance().getReference().child(USER_REF_NAME);

    public static final DatabaseReference favoriteProductRef =
            FirebaseDatabase.getInstance().getReference().child(FAVORITE_PRODUCT_REF_KEY);

    public static final DatabaseReference cartRef =
            FirebaseDatabase.getInstance().getReference().child(CART_REF_KEY);

    public static final DatabaseReference deliveryAddressRef =
            FirebaseDatabase.getInstance().getReference().child(DELIVERY_ADDRESS_REF_NAME);

    public static final DatabaseReference orderRef =
            FirebaseDatabase.getInstance().getReference().child(ORDER_REF_NAME);

    public static final DatabaseReference myVoucherRef =
            FirebaseDatabase.getInstance().getReference().child(MY_VOUCHER_REF_NAME);

    public static final DatabaseReference voucherRef =
            FirebaseDatabase.getInstance().getReference().child(VOUCHER_REF_KEY);

    public static final DatabaseReference coinsRef =
            FirebaseDatabase.getInstance().getReference().child(COINS_REF_NAME);

    public static final DatabaseReference searchHistoriesRef =
            FirebaseDatabase.getInstance().getReference().child(SEARCH_HISTORY_REF_KEY);

    public static final DatabaseReference bestSellerRef =
            FirebaseDatabase.getInstance().getReference().child(BESTSELLER_REF_NAME);
}
