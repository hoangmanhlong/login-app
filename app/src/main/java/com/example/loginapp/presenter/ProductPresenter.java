package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.ProductInterator;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.view.fragments.product_detail.ProductView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter implements ProductListener {

    private Product product;

    public boolean isFavorite = false;

    private ProductView view;

    private ProductInterator interator;

    private List<Comment> comments;

    private List<Product> similarProducts;

    private final boolean authenticated;

    public void initData() {
        if (!comments.isEmpty()) view.getComments(comments);
        if (!similarProducts.isEmpty()) view.getSimilarProducts(similarProducts);
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
        interator = new ProductInterator(this);
        comments = new ArrayList<>();
        similarProducts = new ArrayList<>();
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void onBuyNowButtonClick() {
        if (authenticated) view.showSelectProductQuantityAndVoucherFragment(product);
    }

    public void onAddToCartButtonClick() {
        if (authenticated) view.showAddProductToCartFragment(product);
    }

    public void updateFavorite() {
        if (authenticated) {
            if (isFavorite) interator.removeFavoriteProduct(product.getId());
            else interator.saveFavoriteProduct(product);
        }
    }


    /***
     *  Method check product is favorite from server
     */
    @Override
    public void isFavoriteProduct(Boolean isFavoriteProduct) {
        isFavorite = isFavoriteProduct;
        if (view != null) view.enableFavorite(isFavoriteProduct);
    }

    @Override
    public void saveToBasketSuccess() {
        if (view != null) view.saveToBasketSuccess();
    }

    @Override
    public void getCommentRespond(CommentRespond commentRespond) {
        if (view != null) {
            comments = commentRespond.getComments();
            view.getComments(comments);
            view.getCommentCount(String.valueOf(commentRespond.getLimit()));
        }
    }

    @Override
    public void hasNewFavoriteProduct() {
        if (view != null) view.hasNewFavoriteProduct();
    }

    @Override
    public void getSimilarProducts(List<Product> products) {
        similarProducts = products;
        similarProducts.remove(product);
        if (view != null) view.getSimilarProducts(products);
    }

    public void clear() {
        comments = null;
        product = null;
        similarProducts = null;
        view = null;
        interator.clear();
        interator = null;
    }
}
