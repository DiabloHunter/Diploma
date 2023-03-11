package com.example.project.dto.category;

public class CreateUpdateCategoryDto {

    private String id;
    private String categoryNameEn;
    private String categoryNameUa;
    private String descriptionEn;
    private String descriptionUa;
    private String imageData;

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
