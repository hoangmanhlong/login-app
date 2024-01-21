package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.interator.ProductInterator;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.view.fragment.product_detail.ProductView;

public class ProductPresenter implements ProductListener {

    public Product currentProduct;

    public boolean isFavorite = false;

    private final ProductView view;

    private final ProductInterator interator = new ProductInterator(this);

    public ProductPresenter(ProductView view) {
        this.view = view;
    }

    public void addFavoriteProduct() {
        interator.saveFavoriteProduct(currentProduct);
    }

    public void getProduct(int productId) {
        interator.getProduct(productId);
    }

    @Override
    public void onGetProduct(Product product) {
        view.onLoadProduct(product);
        currentProduct = product;
    }

    public void removeFavorite() {
        interator.removeFavoriteProduct(currentProduct.getId());
    }

    public void addToCart() {
        Product product = currentProduct;
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

    @Override
    public void enableFavorite(Boolean compare) {
        if (compare) {
            isFavorite = true;
            view.enableFavorite(true);
        }
    }

    @Override
    public void isLoading(Boolean loading) {
        view.isLoading(loading);
    }

    @Override
    public void removeSuccess() {
        isFavorite = false;
        view.enableFavorite(false);
    }

    @Override
    public void saveToBasketSuccess() {
        view.saveToBasketSuccess();
    }
}
