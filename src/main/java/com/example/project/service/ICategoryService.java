package com.example.project.service;

import com.example.project.model.Category;

import java.util.List;

public interface ICategoryService {
    void create(Category category) throws Exception;

    List<Category> getAllCategory();

    void update(Long categoryId, Category updateCategory) throws Exception;

    Category getCategoryById(Long categoryId);

    void delete(Long categoryId);
}
