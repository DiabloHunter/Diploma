package com.example.project.service;

import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.model.Category;
import javassist.NotFoundException;

import java.util.List;

public interface ICategoryService {

    List<Category> getAllCategory();

    Category getCategoryById(Long categoryId);

    void create(CreateUpdateCategoryDto createUpdateCategoryDto) throws IllegalArgumentException;

    void update(Long categoryId, CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException;

    void delete(Long categoryId) throws NotFoundException;

}
