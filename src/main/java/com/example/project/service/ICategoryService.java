package com.example.project.service;

import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.model.Category;
import javassist.NotFoundException;

import java.util.List;

public interface ICategoryService {

    List<Category> getAllCategory();

    Category getCategoryById(String categoryId);

    void create(CreateUpdateCategoryDto createUpdateCategoryDto) throws IllegalArgumentException;

    void update(String categoryId, CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException;

    void delete(String categoryId) throws NotFoundException;

}
