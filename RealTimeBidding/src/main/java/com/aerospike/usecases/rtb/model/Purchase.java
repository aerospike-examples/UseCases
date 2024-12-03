package com.aerospike.usecases.rtb.model;

import java.util.Date;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.NoArgsConstructor;

/**
 * Represents a purchase made by a user and is part of a UserProfile
 */
@NoArgsConstructor
@AerospikeRecord
public class Purchase {
    // purchase id
    private String id;
    // purchase date
    private Date purchaseDate;
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

    public Purchase(String id, Date purchaseDate, String productSku, String description, double price, String currency,
            String category, String subCategory, String brand, String model, String color, String size,
            String condition, String vendorUrl, String productUrl, String activityEventId) {
        this.id = id;
        this.purchaseDate = new Date();
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

    public Date getPurchaseDate() {
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

    @Override
    public String toString() {
        return "Purchase{" + "id='" + id + '\'' + ", purchaseDate=" + purchaseDate + ", productSku='" + productSku
                + '\'' + ", description='" + description + '\'' + ", price=" + price + ", currency='" + currency + '\''
                + ", category='" + category + '\'' + ", subCategory='" + subCategory + '\'' + ", brand='" + brand + '\''
                + ", model='" + model + '\'' + ", color='" + color + '\'' + ", size='" + size + '\'' + ", condition='"
                + condition + '\'' + ", vendorUrl='" + vendorUrl + '\'' + ", productUrl='" + productUrl + '\''
                + ", activityEventId='" + activityEventId + '\'' + '}';
    }

}
