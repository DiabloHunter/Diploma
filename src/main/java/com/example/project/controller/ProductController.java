package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.productDto.ProductDTO;
import com.example.project.dto.productDto.ProductIoTDTO;
import com.example.project.model.Category;
import com.example.project.model.Product;
import com.example.project.service.ICategoryService;
import com.example.project.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    IProductService productService;

    @Autowired
    ICategoryService categoryService;

    private static final String CHECK_PRICES_CRON = "0 58 13 ? * SAT";

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDTO productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
         if (category==null) {
             return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
         }
        try {
            productService.addProduct(productDto, category);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getByCode/")
    public ResponseEntity<ProductIoTDTO> getProductByCode(@RequestBody ProductDTO productDto) {
        Product product = productService.getProductByCode(productDto.getCode());
        ProductIoTDTO productIoTDTO = new ProductIoTDTO(product.getId(), product.getCode(), product.getName(),
                product.getPrice(), product.getDescription());
        return new ResponseEntity<>(productIoTDTO, HttpStatus.OK);
    }


    @Scheduled(cron = CHECK_PRICES_CRON)
    public ResponseEntity<ApiResponse>  checkPricesSchedule(){
        productService.checkPrices();
        return new ResponseEntity<>(new ApiResponse(true, "Date for all products have been changed"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/checkPrices")
    public ResponseEntity<ApiResponse>  checkPrices(){
        productService.checkPrices();
        return new ResponseEntity<>(new ApiResponse(true, "Date for all products have been changed"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDTO productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
        if (category==null) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        try {
            productService.updateProduct(productDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long id){
        if (productService.findProductById(id)==null) {
            return new ResponseEntity<>(new ApiResponse(false, "product does not exists"), HttpStatus.NOT_FOUND);
        }
        productService.deleteProductById(id);
        return new ResponseEntity<>(new ApiResponse(true, "product has been deleted"), HttpStatus.OK);
    }

}
