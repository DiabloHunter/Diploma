package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.category.CategoryRequestDTO;
import com.example.project.model.Category;
import com.example.project.service.ICategoryService;
import com.example.project.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "a new category created"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<Category> listCategory() {
        return categoryService.getAllCategory();
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category) {
        if (categoryService.getCategoryById(category.getId())==null) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.NOT_FOUND);
        }
        try {
            categoryService.editCategory(category.getId(), category);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "category has been updated"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete/")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestBody CategoryRequestDTO categoryRequestDTO){
        if (categoryService.getCategoryById(categoryRequestDTO.getCategoryId())==null) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.NOT_FOUND);
        }
        categoryService.deleteCategory(categoryRequestDTO.getCategoryId());
        return new ResponseEntity<>(new ApiResponse(true, "category has been deleted"), HttpStatus.OK);
    }
}
