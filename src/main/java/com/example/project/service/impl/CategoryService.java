package com.example.project.service.impl;

import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.model.Category;
import com.example.project.repository.ICategoryRepository;
import com.example.project.service.ICategoryService;
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

        Category category = new Category(createUpdateCategoryDto.getCategoryName(),
                createUpdateCategoryDto.getDescription(), createUpdateCategoryDto.getImageData());
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void update(String categoryId, CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException {
        Category updated = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with Id %s was not found!", categoryId)));
        Category existedCategory = categoryRepository.findByCategoryName(createUpdateCategoryDto.getCategoryName());
        if (existedCategory != null && updated.getId() == existedCategory.getId()) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    updated.getCategoryName()));
        }
        updated.setCategoryName(createUpdateCategoryDto.getCategoryName());
        updated.setDescription(createUpdateCategoryDto.getDescription());
        updated.setImageData(createUpdateCategoryDto.getImageData());
        categoryRepository.save(updated);
    }

    @Override
    public Category getCategoryById(String categoryId) {
        return categoryRepository.getById(categoryId);
    }

    @Override
    public void delete(String categoryId) throws NotFoundException {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(String.format("Category with Id %s was not found!", categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }
}
