package com.example.project.dto.category;

public class CreateUpdateCategoryDto {

    private String id;
    private String nameEn;
    private String nameUa;
    private String descriptionEn;
    private String descriptionUa;
    private String imageData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
