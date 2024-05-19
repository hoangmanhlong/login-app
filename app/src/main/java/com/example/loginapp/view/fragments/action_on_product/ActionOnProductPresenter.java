package com.example.loginapp.view.fragments.action_on_product;

import com.example.loginapp.model.entity.Product;

public class ActionOnProductPresenter implements ActionOnProductListener {

    private ActionOnProductInteractor interactor;

    /**
     * used to update UI in {@link  ActionOnProductFragment}
     */
    private ActionOnProductView view;

    /**
     * product is clicked by user
     */
    private Product product;

    /**
     * update Argument received from {@link com.example.loginapp.view.fragments.cart.CartFragment} or
     * {@link com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment}
     *
     * @param openedFromCart {@link ActionOnProductFragment} opened from {@link com.example.loginapp.view.fragments.cart.CartFragment} or not
     */
    public void setOpenedFromCart(Boolean openedFromCart) {
        this.openedFromCart = openedFromCart;
        if (view != null) view.updateView(openedFromCart);
    }

    /**
     * This value used to check {@link ActionOnProductFragment} created by which screen
     */
    Boolean openedFromCart;

    /**
     * Hold ref to {@link ActionOnProductFragment} used to update UI
     *
     * @param view ref to UI
     */
    public ActionOnProductPresenter(ActionOnProductView view) {
        this.view = view;
        interactor = new ActionOnProductInteractor(this);
    }

    /**
     * update Argument received from {@link com.example.loginapp.view.fragments.cart.CartFragment} or
     * {@link com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment}
     *
     * @param product is the product selected by the user
     */
    public void setProduct(Product product) {
        this.product = product;
        if (view != null) view.bindProduct(product);
    }

    /**
     * Remove Product from Basket or Wishlist
     */
    public void removeProduct() {
        if (view != null) view.removeProduct(product.getId());
    }

    /**
     * Add this product into Basket or wishlist
     */
    public void addProduct() {
        if (openedFromCart) interactor.saveFavoriteProduct(product);
        else view.openAddToCartFragment(product);
    }

    @Override
    public void addToWishlistSuccess() {
        view.dismissFragment();
    }
}
