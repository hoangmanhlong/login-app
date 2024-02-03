package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.ProductInterator;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.view.fragment.product_detail.ProductView;

public class ProductPresenter implements ProductListener {

    private Product product;

    public boolean isFavorite = false;

    private final ProductView view;

    public Voucher voucher;

    private final ProductInterator interator = new ProductInterator(this);

    public void initData() {

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        view.bindProduct(product);
        interator.isFavoriteProduct(product);
    }

    public ProductPresenter(ProductView view) {
        this.view = view;
    }

    public void updateFavorite() {
        if (isFavorite) interator.removeFavoriteProduct(product.getId());
        else interator.saveFavoriteProduct(product);
    }

    public void addProductToCart() {
        interator.updateQuantity(
                new FirebaseProduct(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getDiscountPercentage(),
                        product.getRating(),
                        product.getStock(),
                        product.getBrand(),
                        product.getCategory(),
                        product.getThumbnail(),
                        product.getImages()
                ));
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }


    /***
     *  Method check product is favorite from server
     */
    @Override
    public void isFavoriteProduct(Boolean isFavoriteProduct) {
        isFavorite = isFavoriteProduct;
        view.enableFavorite(isFavoriteProduct);
    }

    /***
     * Get comments from API
     */
    public void getComments() {
        interator.getComments();
    }

    @Override
    public void saveToBasketSuccess() {
        view.saveToBasketSuccess();
    }

    @Override
    public void getCommentRespond(CommentRespond commentRespond) {
        view.getComments(commentRespond.getComments());
        view.getCommentCount(String.valueOf(commentRespond.getLimit()));
    }

    @Override
    public void hasNewFavoriteProduct() {
        view.hasNewFavoriteProduct();
    }
}
