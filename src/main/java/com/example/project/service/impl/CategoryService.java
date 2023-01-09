package com.example.project.service.impl;

import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.model.Category;
import com.example.project.repository.ICategoryRepository;
import com.example.project.service.ICategoryService;
import com.example.project.util.ValidationUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public void create(CreateUpdateCategoryDto createUpdateCategoryDto) throws IllegalArgumentException {
        if (categoryRepository.existsByCategoryName(createUpdateCategoryDto.getCategoryName())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    createUpdateCategoryDto.getCategoryName()));
        }
        ValidationUtil.validateImageUrl(createUpdateCategoryDto.getImageUrl());

        Category category = new Category(createUpdateCategoryDto.getCategoryName(),
                createUpdateCategoryDto.getDescription(), createUpdateCategoryDto.getImageUrl());
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void update(Long categoryId, CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with Id %s was not found!", categoryId)));
        if (categoryRepository.existsByCategoryName(createUpdateCategoryDto.getCategoryName())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    category.getCategoryName()));
        }
        ValidationUtil.validateImageUrl(createUpdateCategoryDto.getImageUrl());
        category.setCategoryName(createUpdateCategoryDto.getCategoryName());
        category.setDescription(createUpdateCategoryDto.getDescription());
        category.setImageUrl(createUpdateCategoryDto.getImageUrl());
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.getById(categoryId);
    }

    @Override
    public void delete(Long categoryId) throws NotFoundException {
        if(!categoryRepository.existsById(categoryId)){
            throw new NotFoundException(String.format("Category with Id %s was not found!", categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }
}
