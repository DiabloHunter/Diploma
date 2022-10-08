package com.example.project.service;

import com.example.project.dto.productDto.ProductDTO;
import com.example.project.exceptions.ProductNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Product;

import java.util.Date;
import java.util.List;

public interface IProductService {
    void addProduct(ProductDTO productDto, Category category) throws Exception;

    ProductDTO getProductDto(Product product);

    Product getProductByCode(String code);

    List<ProductDTO> getAllProducts();

    void updateProduct(ProductDTO productDto) throws Exception;

    Product findProductById(Long productId) throws ProductNotExistsException;

    void deleteProductById(Long productId);

    Date convertDate();

    void checkPrices();
}
