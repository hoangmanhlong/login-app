package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.ProductInterator;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.view.fragments.product_detail.ProductView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter implements ProductListener {

    private Product product;

    public boolean isFavorite = false;

    private final ProductView view;

    public Voucher voucher;

    private final ProductInterator interator = new ProductInterator(this);

    private List<Comment> comments = new ArrayList<>();

    private List<Product> similarProducts = new ArrayList<>();

    private final boolean authenticated;

    public void initData() {
        if (comments.isEmpty()) getComments();
        else view.getComments(comments);

        if (similarProducts.isEmpty()) getSimilarProducts();
        else view.getSimilarProducts(similarProducts);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        view.bindProduct(product);
        if (authenticated) interator.isFavoriteProduct(product);
    }

    public ProductPresenter(ProductView view) {
        this.view = view;
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }


    public void updateFavorite() {
        if (authenticated) {
            if (isFavorite) interator.removeFavoriteProduct(product.getId());
            else interator.saveFavoriteProduct(product);
        }
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
        comments = commentRespond.getComments();
        view.getComments(comments);
        view.getCommentCount(String.valueOf(commentRespond.getLimit()));
    }

    @Override
    public void hasNewFavoriteProduct() {
        view.hasNewFavoriteProduct();
    }

    @Override
    public void getSimilarProducts(List<Product> products) {
        similarProducts = products;
        similarProducts.remove(product);
        view.getSimilarProducts(products);
    }

    public void getSimilarProducts() {
        interator.getSimilarProducts(product.getCategory());
    }
}
