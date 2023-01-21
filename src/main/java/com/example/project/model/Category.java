package com.example.project.model;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private @NotBlank String categoryName;
    private @NotBlank String description;
    @Lob
    @Column(length = 100000)
    private @NotBlank String imageData;

    public Category(Long id, String categoryName, String description, String imageData) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.imageData = imageData;
    }

    public Category(String categoryName, String description, String imageData) {
        this.categoryName = categoryName;
        this.description = description;
        this.imageData = imageData;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
