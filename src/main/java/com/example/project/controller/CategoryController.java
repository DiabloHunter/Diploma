package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ProductDto;
import com.example.project.model.Category;
import com.example.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


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
        return categoryService.listCategory();
    }


    @PostMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category ) {
        if (!categoryService.findById(categoryId)) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.NOT_FOUND);
        }
        try {
            categoryService.editCategory(categoryId, category);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "category has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") int categoryId){
        if (!categoryService.findById(categoryId)) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.NOT_FOUND);
        }
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse(true, "category has been deleted"), HttpStatus.OK);
    }
}
