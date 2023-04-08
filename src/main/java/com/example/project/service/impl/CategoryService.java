package com.example.project.service.impl;

import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.dto.filter.FilterCategoryDTO;
import com.example.project.model.Category;
import com.example.project.repository.ICategoryRepository;
import com.example.project.service.ICategoryService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public void create(CreateUpdateCategoryDto createUpdateCategoryDto) throws IllegalArgumentException {
        if (categoryRepository.existsByNameEn(createUpdateCategoryDto.getNameEn())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    createUpdateCategoryDto.getNameEn()));
        }

        if (categoryRepository.existsByNameUa(createUpdateCategoryDto.getNameUa())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    createUpdateCategoryDto.getNameUa()));
        }

        Category category = new Category(createUpdateCategoryDto.getNameEn(),
                createUpdateCategoryDto.getNameUa(), createUpdateCategoryDto.getDescriptionEn(),
                createUpdateCategoryDto.getDescriptionUa(), createUpdateCategoryDto.getImageData());
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getFilteredCategories(FilterCategoryDTO filterCategoryDTO) {
        List<Category> categories = categoryRepository.findAll();
        String filterName = filterCategoryDTO.getName();
        if (filterName != null) {
            categories.retainAll(categories.stream().filter(category -> category.getNameEn().equals(filterName) ||
                    category.getNameUa().equals(filterName)).collect(Collectors.toList()));
        }

        return categories;
    }

    @Override
    public void update(String categoryId, CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException {
        Category updated = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with Id %s was not found!", categoryId)));

        Category existedCategoryByEnName = categoryRepository.findByNameEn(createUpdateCategoryDto.getNameEn());
        Category existedCategoryByUaName = categoryRepository.findByNameUa(createUpdateCategoryDto.getNameUa());
        if (existedCategoryByEnName != null && !updated.getId().equals(existedCategoryByEnName.getId())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    updated.getNameEn()));
        }

        if (existedCategoryByUaName != null && !updated.getId().equals(existedCategoryByUaName.getId())) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists!",
                    updated.getNameUa()));
        }

        updated.setNameEn(createUpdateCategoryDto.getNameEn());
        updated.setNameUa(createUpdateCategoryDto.getNameUa());
        updated.setDescriptionEn(createUpdateCategoryDto.getDescriptionEn());
        updated.setDescriptionUa(createUpdateCategoryDto.getDescriptionUa());
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
