package com.example.project.service;

import com.example.project.model.Category;

import java.util.List;

public interface ICategoryService {
    void createCategory(Category category) throws Exception;

    List<Category> getAllCategory();

    void editCategory(Long categoryId, Category updateCategory) throws Exception;

    Category getCategoryById(Long categoryId);

    void deleteCategory(Long categoryId);
}
