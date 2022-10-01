package com.example.project.dto.category;

import com.example.project.model.Category;

public class CategoryRequestDTO {

    private Long categoryId;

    private Category category;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
