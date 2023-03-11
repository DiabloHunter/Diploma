package com.example.project.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private @NotBlank String categoryNameEn;
    private @NotBlank String categoryNameUa;
    private @NotBlank String descriptionEn;
    private @NotBlank String descriptionUa;
    @Lob
    @Column(length = 100000)
    private @NotBlank String imageData;

    public Category(String categoryNameEn, String categoryNameUa, String descriptionEn, String descriptionUa, String imageData) {
        this.categoryNameEn = categoryNameEn;
        this.categoryNameUa = categoryNameUa;
        this.descriptionEn = descriptionEn;
        this.descriptionUa = descriptionUa;
        this.imageData = imageData;
    }

    public Category(String id, String categoryNameEn, String categoryNameUa, String descriptionEn, String descriptionUa, String imageData) {
        this.id = id;
        this.categoryNameEn = categoryNameEn;
        this.categoryNameUa = categoryNameUa;
        this.descriptionEn = descriptionEn;
        this.descriptionUa = descriptionUa;
        this.imageData = imageData;
    }

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        this.categoryNameEn = categoryNameEn;
    }

    public String getCategoryNameUa() {
        return categoryNameUa;
    }

    public void setCategoryNameUa(String categoryNameUa) {
        this.categoryNameUa = categoryNameUa;
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

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
