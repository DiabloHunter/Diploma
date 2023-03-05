package com.example.project.dto.statistic;

import javax.validation.constraints.NotNull;

public class DishStatisticDTO {

    private String id;
    private @NotNull String searchId;
    private @NotNull String name;
    private @NotNull String imageData;
    private @NotNull double price;
    private @NotNull String descriptionEn;
    private @NotNull String descriptionUa;
    private @NotNull String categoryId;
    private @NotNull double monthSales;
    private @NotNull int place;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public double getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(double monthSales) {
        this.monthSales = monthSales;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
