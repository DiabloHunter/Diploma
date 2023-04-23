package com.example.project.dto.statistic;

import javax.validation.constraints.NotNull;

public class DishStatisticDTO {

    private String id;
    private @NotNull String searchId;
    private @NotNull String nameEn;
    private @NotNull String nameUa;
    private @NotNull double price;
    private @NotNull String descriptionEn;
    private @NotNull String descriptionUa;
    private @NotNull String categoryId;
    private @NotNull double monthSales;

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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameUa() {
        return nameUa;
    }

    public void setNameUa(String nameUa) {
        this.nameUa = nameUa;
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

}
