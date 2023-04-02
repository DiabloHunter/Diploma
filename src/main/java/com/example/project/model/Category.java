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
    private @NotBlank String nameEn;
    private @NotBlank String nameUa;
    private @NotBlank String descriptionEn;
    private @NotBlank String descriptionUa;
    @Lob
    @Column(length = 100000)
    private @NotBlank String imageData;

    public Category(String nameEn, String nameUa, String descriptionEn, String descriptionUa, String imageData) {
        this.nameEn = nameEn;
        this.nameUa = nameUa;
        this.descriptionEn = descriptionEn;
        this.descriptionUa = descriptionUa;
        this.imageData = imageData;
    }

    public Category(String id, String nameEn, String nameUa, String descriptionEn, String descriptionUa, String imageData) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameUa = nameUa;
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
