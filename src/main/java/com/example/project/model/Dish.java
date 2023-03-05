package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private @NotNull String searchId;
    private @NotNull String name;
    @Lob
    @Column(length = 100000)
    private @NotNull String imageData;
    private @NotNull Double price;
    private @NotNull String descriptionEn;
    private @NotNull String descriptionUa;
    private @NotNull LocalDateTime checkDate;
    private @NotNull Double minSales;
    private @NotNull Double maxSales;
    private @NotNull Double costPrice;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnore
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Category category;

    public Dish(String searchId, String name, String imageData, Double price, String descriptionEn, String descriptionUa,
                LocalDateTime checkDate, Double minSales, Double maxSales, Double costPrice, Category category) {
        this.searchId = searchId;
        this.name = name;
        this.imageData = imageData;
        this.price = price;
        this.descriptionEn = descriptionEn;
        this.descriptionUa = descriptionUa;
        this.checkDate = checkDate;
        this.minSales = minSales;
        this.maxSales = maxSales;
        this.costPrice = costPrice;
        this.category = category;
    }

    public Dish() {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public Double getMinSales() {
        return minSales;
    }

    public void setMinSales(Double minSales) {
        this.minSales = minSales;
    }

    public Double getMaxSales() {
        return maxSales;
    }

    public void setMaxSales(Double maxSales) {
        this.maxSales = maxSales;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
