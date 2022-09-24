package com.example.project.service;

import com.example.project.dto.productDto.ProductDto;
import com.example.project.exceptions.ProductNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Product;

import java.util.Date;
import java.util.List;

public interface IProductService {
    void addProduct(ProductDto productDto, Category category) throws Exception;

    ProductDto getProductDto(Product product);

    Product getProductByCode(String code);

    List<ProductDto> getAllProducts();

    void updateProduct(ProductDto productDto) throws Exception;

    Product findProductById(Long productId) throws ProductNotExistsException;

    void deleteProductById(Long productId);

    Date convertDate();

    void checkPrices(Date change);
}
