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
    ICategoryRepository ICategoryRepository;

    @Override
    public void createCategory(Category category) throws Exception {
        Category categoryInBd = ICategoryRepository.findCategoryByCategoryName(category.getCategoryName());
        if(categoryInBd!=null){
            throw new Exception("Category with the same name has already existed!");
        }
        validateCategoryImage(category);
        ICategoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return ICategoryRepository.findAll();
    }

    @Override
    public void editCategory(Long categoryId, Category updateCategory) throws Exception {
        Category category = ICategoryRepository.findById(categoryId).orElse(null);
        Category categoryInDb = ICategoryRepository.findCategoryByCategoryName(updateCategory.getCategoryName());
        if(categoryInDb!=null && category.getId()!=categoryInDb.getId()){
            throw new Exception("Category with the same code has already existed!");
        }
        validateCategoryImage(updateCategory);
        category.setCategoryName(updateCategory.getCategoryName());
        category.setDescription(updateCategory.getDescription());
        category.setImageUrl(updateCategory.getImageUrl());
        ICategoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return ICategoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public void deleteCategory(Long categoryId){
        ICategoryRepository.deleteById(categoryId);
    }


    private void validateCategoryImage(Category category) throws Exception {
        if(category.getImageUrl().length()>240){
            throw new Exception("Image URL is too long!");
        }
    }
}
