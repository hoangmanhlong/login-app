package com.example.loginapp.model.entity;

import java.text.NumberFormat;
import java.util.List;

public class FirebaseProduct {
    private int id;
    private String title;
    private String description;
    private int price;
    private double discountPercentage;
    private double rating;
    private int stock;
    private String brand;
    private String category;
    private String thumbnail;
    private List<String> images;

    private int quantity = 1;

    private boolean isChecked = false;

    public void setTitle(String title) {
        this.title = title;
    }

    public FirebaseProduct() {
    }

    public FirebaseProduct(
        int id,
        String title,
        String description,
        int price,
        double discountPercentage,
        double rating,
        int stock,
        String brand,
        String category,
        String thumbnail,
        List<String> images
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.thumbnail = thumbnail;
        this.images = images;
    }

    public FirebaseProduct(int id, String title, String description, int price, double discountPercentage, double rating, int stock, String brand, String category, String thumbnail, List<String> images, int quantity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.thumbnail = thumbnail;
        this.images = images;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getRating() {
        return rating;
    }

    public int getStock() {
        return stock;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public List<String> getImages() {
        return images;
    }


    public int getQuantity() {
        return quantity;
    }

    public String intToString() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(price);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Product toProduct() {
        return new Product(this.id, this.title, this.description, this.price, this.discountPercentage, this.rating, this.stock, this.brand, this.category, this.thumbnail, this.images);
    }

    public OrderProduct toOrderProduct() {
        return new OrderProduct(this.id, this.title, this.description, this.price, this.discountPercentage, this.rating, this.stock, this.brand, this.category, this.thumbnail, this.images, this.quantity);
    }

    public static final String QUANTITY = "quantity";
}


