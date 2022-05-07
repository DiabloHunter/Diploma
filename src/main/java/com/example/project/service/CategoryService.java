package com.example.project.service;

import com.example.project.dto.ProductDto;
import com.example.project.model.Category;
import com.example.project.model.Product;
import com.example.project.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public void createCategory(Category category) throws Exception {
        Category categoryInBd = categoryRepo.findCategoryByCategoryName(category.getCategoryName());
        if(categoryInBd!=null){
            throw new Exception("Category with the same name has already existed!");
        }
        validateProduct(category);
        categoryRepo.save(category);
    }

    public List<Category> listCategory() {
        return categoryRepo.findAll();
    }

    public void editCategory(int categoryId, Category updateCategory) throws Exception {
        Category category = categoryRepo.getById(categoryId);
        Category categoryInDb = categoryRepo.findCategoryByCategoryName(updateCategory.getCategoryName());
        // throw an exception if product does not exists
        if(categoryInDb!=null && category.getId()!=categoryInDb.getId()){
            throw new Exception("Category with the same code has already existed!");
        }
        validateProduct(updateCategory);
        category.setCategoryName(updateCategory.getCategoryName());
        category.setDescription(updateCategory.getDescription());
        category.setImageUrl(updateCategory.getImageUrl());
        categoryRepo.save(category);
    }

    public boolean findById(int categoryId) {
        return categoryRepo.findById(categoryId).isPresent();
    }

    public void deleteCategory(int categoryId){
        categoryRepo.deleteById(categoryId);
    }

    private void validateProduct(Category category) throws Exception {
        if(category.getImageUrl().length()>240){
            throw new Exception("Image URL is too long!");
        }
    }
}
