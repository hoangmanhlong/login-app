package com.example.loginapp.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class Constant {

    public static final String DUMMY_JSON_URL = "https://dummyjson.com/";

    public static final String VIETNAM_COUNTRY_CODE = "vi";

    public static final String OPENED_FROM_BUY_NOW_OF_ACTION_ON_PRODUCT_FRAGMENT = "o23yh680x7t348c4rt4";

    public static final String OPENED_FROM_CART = "is_product_in_cart";

    public static final String ENGLISH_COUNTRY_CODE = "en-US";

    public static final String VIETNAM = "Việt Nam";

    public static final String IS_VIETNAMESE_LANGUAGE = "is_vietnamese_language";

    public static final int BACK_PRESS_INTERVAL = 2000;

    public static final String APP_PREFERENCE_KEY = "app_preference_key";

    public static final String IS_VIEWED_FAVORITES_LIST_KEY = "isViewedFavoritesList";

    public static final String EXPAND_LABEL_KEY = "expand_label";

    public static final String EXPAND_PRODUCTS_KEY = "expand";

    public static final String PHONE_NUMBER_KEY = "phone_number";

    public static final String DELIVERY_ADDRESS_KEY = "delivery_address_key";

    private static final String USER_REF_NAME = "user_personal_information";

    public static final String STORAGE_USER_AVATAR_REF_NAME = "avatar";

    public static final String STORAGE_USER_DATA_REF_NAME = "user_data";

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

    public static final StorageReference avatarUserRef =
            FirebaseStorage.getInstance().getReference()
                    .child(STORAGE_USER_DATA_REF_NAME)
                    .child(STORAGE_USER_AVATAR_REF_NAME);
}
