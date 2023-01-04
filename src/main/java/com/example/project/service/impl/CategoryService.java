package com.example.project.service.impl;

import com.example.project.model.Category;
import com.example.project.repository.ICategoryRepository;
import com.example.project.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public void create(Category category) throws Exception {
        Category categoryInBd = categoryRepository.findCategoryByCategoryName(category.getCategoryName());
        if (categoryInBd != null) {
            throw new Exception("Category with the same name already exists!");
        }
        validateCategoryImage(category);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void update(Long categoryId, Category updateCategory) throws Exception {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Category categoryInDb = categoryRepository.findCategoryByCategoryName(updateCategory.getCategoryName());
        if (categoryInDb != null && category.getId() != categoryInDb.getId()) {
            throw new Exception("Category with the same searchId already exists!");
        }
        validateCategoryImage(updateCategory);
        category.setCategoryName(updateCategory.getCategoryName());
        category.setDescription(updateCategory.getDescription());
        category.setImageUrl(updateCategory.getImageUrl());
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }


    private void validateCategoryImage(Category category) throws Exception {
        if (category.getImageUrl().length() > 240) {
            throw new Exception("Image URL is too long!");
        }
    }
}
