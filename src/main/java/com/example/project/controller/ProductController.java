package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.productDto.ProductDto;
import com.example.project.dto.productDto.ProductDtoIoT;
import com.example.project.model.Category;
import com.example.project.model.Product;
import com.example.project.service.ICategoryService;
import com.example.project.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    IProductService productService;

    @Autowired
    ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDto productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
//         if (!optionalCategory.isPresent()) {
//             return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
//         }
        try {
            productService.addProduct(productDto, category);
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

    @GetMapping("/{code}")
    public ResponseEntity<ProductDtoIoT> getProductByCode(@PathVariable("code") String code) {
        Product product = productService.getProductByCode(code);
        ProductDtoIoT productDtoIoT = new ProductDtoIoT(product.getId(), product.getCode(), product.getName(),
                product.getPrice(), product.getDescription());
        return new ResponseEntity<>(productDtoIoT, HttpStatus.OK);
    }

    @PostMapping("/checkPrices")
    public ResponseEntity<ApiResponse>  checkPrices(){
        Date checkDate = productService.convertDate();
        productService.checkPrices(checkDate);
        return new ResponseEntity<>(new ApiResponse(true, "Date for all products have been changed"), HttpStatus.CREATED);
    }

    // create an api to edit the product


    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDto productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
//        if (!optionalCategory.isPresent()) {
//            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
//        }
        try {
            productService.updateProduct(productDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("productId") long productId){
        if (productService.findProductById(productId)==null) {
            return new ResponseEntity<>(new ApiResponse(false, "product does not exists"), HttpStatus.NOT_FOUND);
        }
        productService.deleteProductById(productId);
        return new ResponseEntity<>(new ApiResponse(true, "product has been deleted"), HttpStatus.OK);
    }

}
