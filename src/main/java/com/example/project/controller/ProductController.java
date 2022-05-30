package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.ProductDto;
import com.example.project.model.Category;
import com.example.project.repository.CategoryRepo;
import com.example.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDto productDto) {
         Optional<Category> optionalCategory = categoryRepo.findById(productDto.getCategoryId());
         if (!optionalCategory.isPresent()) {
             return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
         }
        try {
            productService.createProduct(productDto, optionalCategory.get());
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/checkPrices")
    public ResponseEntity<ApiResponse>  checkPrices(){
        Date checkDate = productService.convertDate();
        productService.checkPrices(checkDate);
        return new ResponseEntity<>(new ApiResponse(true, "Date for all products have been changed"), HttpStatus.CREATED);
    }

    // create an api to edit the product


    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDto productDto) {
        Optional<Category> optionalCategory = categoryRepo.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        try {
            productService.updateProduct(productDto, productId);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("productId") int productId){
        if (productService.findById(productId)==null) {
            return new ResponseEntity<>(new ApiResponse(false, "product does not exists"), HttpStatus.NOT_FOUND);
        }
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse(true, "product has been deleted"), HttpStatus.OK);
    }

}
