package com.aerospike.usecases.rtb.model;

import java.time.LocalDateTime;

public class Purchase {
    // purchase id
    private String id;
    // purchase date
    private LocalDateTime purchaseDate;
    // product Stock Keeping Unit
    private String productSku;
    private String description;
    // price paid for the product
    private double price;
    // currency of the price
    private String currency;
    // product category
    private String category;
    // product sub-category
    private String subCategory;
    // product brand
    private String brand;
    // product model
    private String model;
    // product color
    private String color;
    // product size
    private String size;
    // product condition
    private String condition;
    // product vendor
    private String vendorUrl;
    // product URL
    private String productUrl;
    // activity event id
    private String activityEventId;

    public Purchase(String id, LocalDateTime purchaseDate, String productSku, String description, double price,
            String currency, String category, String subCategory, String brand, String model, String color, String size,
            String condition, String vendorUrl, String productUrl, String activityEventId) {
        this.id = id;
        this.purchaseDate = LocalDateTime.now();
        this.productSku = productSku;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.size = size;
        this.condition = condition;

        this.vendorUrl = vendorUrl;
        this.productUrl = productUrl;
        this.activityEventId = activityEventId;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public String getProductSku() {
        return productSku;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getCondition() {
        return condition;
    }

    public String getVendorUrl() {
        return vendorUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getActivityEventId() {
        return activityEventId;
    }

}
