package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.data.remote.api.dto.CommentDto;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.ProductInteractor;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.view.fragments.product_detail.ProductView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter implements ProductListener {

    private Product product;

    public Boolean isFavorite = false;

    private ProductView view;

    private ProductInteractor interactor;

    private List<Comment> comments;

    private List<Product> similarProducts;

    private final boolean authenticated;

    private Boolean retrievedComments = false;

    private Boolean retrievedSimilarProducts = false;

    public void initData() {
        if (product != null) {
            view.bindProduct(product);
            view.enableFavorite(isFavorite);
        } else {
            view.getDataShared();
        }
        if (retrievedComments) {
            view.getComments(comments);
        }
        if (retrievedSimilarProducts) view.getSimilarProducts(similarProducts);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        view.bindProduct(product);
        if (authenticated) interactor.isFavoriteProduct(product);
    }

    public ProductPresenter(ProductView view) {
        this.view = view;
        interactor = new ProductInteractor(this);
        comments = new ArrayList<>();
        similarProducts = new ArrayList<>();
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void onBuyNowButtonClick() {
        if (authenticated && view != null) view.showSelectProductQuantityAndVoucherFragment(product);
    }

    public void onAddToCartButtonClick() {
        if (authenticated && view != null) view.showAddProductToCartFragment(product);
    }

    public void updateFavorite() {
        if (authenticated) {
            if (isFavorite) interactor.removeFavoriteProduct(product.getId());
            else interactor.saveFavoriteProduct(product);
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
    public void getCommentRespond(CommentDto commentDto) {
        if (view != null) {
            retrievedComments = true;
            comments = commentDto.getComments();
            view.getComments(comments);
            view.getCommentCount(String.valueOf(commentDto.getLimit()));
        }
    }

    @Override
    public void getSimilarProducts(List<Product> products) {
        retrievedSimilarProducts = true;
        similarProducts = products;
        similarProducts.remove(product);
        if (view != null) view.getSimilarProducts(products);
    }

    public void clear() {
        isFavorite = null;
        comments = null;
        product = null;
        similarProducts = null;
        view = null;
        interactor.clear();
        interactor = null;
        retrievedComments = null;
        retrievedSimilarProducts = null;
    }
}
