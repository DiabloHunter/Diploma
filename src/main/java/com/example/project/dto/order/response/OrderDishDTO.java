package com.example.project.dto.order.response;

import javax.validation.constraints.NotNull;

public class OrderDishDTO {
    private @NotNull String dishId;
    private @NotNull String name;
    private @NotNull String searchId;
    private @NotNull String imageData;
    private @NotNull Double price;
    private @NotNull String descriptionEn;
    private @NotNull String descriptionUa;
    private @NotNull Double quantity;
    private @NotNull String categoryId;

    public OrderDishDTO(String dishId, String name, String searchId, String imageData, Double price, String descriptionEn,
                        String descriptionUa, Double quantity, String categoryId) {
        this.dishId = dishId;
        this.name = name;
        this.searchId = searchId;
        this.imageData = imageData;
        this.price = price;
        this.descriptionEn = descriptionEn;
        this.descriptionUa = descriptionUa;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    public OrderDishDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionUa() {
        return descriptionUa;
    }

    public void setDescriptionUa(String descriptionUa) {
        this.descriptionUa = descriptionUa;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
