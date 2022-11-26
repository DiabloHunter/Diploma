package com.example.project.dto.dish;

import javax.validation.constraints.NotNull;

public class DishDTO {
    private Long id;
    private @NotNull String searchId;
    private @NotNull String name;
    private @NotNull String imageURL;
    private @NotNull double price;
    private @NotNull String description;
    private @NotNull Long categoryId;
    private @NotNull double minSales;
    private @NotNull double maxSales;
    private @NotNull double costPrice;


    public DishDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMinSales() {
        return minSales;
    }

    public void setMinSales(double minSales) {
        this.minSales = minSales;
    }

    public double getMaxSales() {
        return maxSales;
    }

    public void setMaxSales(double maxSales) {
        this.maxSales = maxSales;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
}
