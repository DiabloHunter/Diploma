package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.category.CreateUpdateCategoryDto;
import com.example.project.model.Category;
import com.example.project.service.ICategoryService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private static final Logger LOG = LogManager.getLogger(CategoryController.class);

    @Autowired
    ICategoryService categoryService;

   // @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CreateUpdateCategoryDto category) {
        categoryService.create(category);

        LOG.info(String.format("Category with name %s has been created!", category.getCategoryName()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Category with name %s has been created!", category.getCategoryName())), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<Category> listCategory() {
        return categoryService.getAllCategory();
    }

   // @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CreateUpdateCategoryDto createUpdateCategoryDto) throws NotFoundException {
        Long categoryId = createUpdateCategoryDto.getId();
        categoryService.update(categoryId, createUpdateCategoryDto);

        LOG.info(String.format("Category with Id %s has been updated!", categoryId));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Category with Id %s has been updated!", categoryId)), HttpStatus.OK);
    }

   // @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete/")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long id) throws NotFoundException {
        categoryService.delete(id);

        LOG.info(String.format("Category with Id %s has been deleted!", id));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Category with Id %s has been deleted!", id)), HttpStatus.OK);
    }
}
