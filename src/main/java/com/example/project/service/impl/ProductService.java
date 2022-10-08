package com.example.project.service.impl;

import com.example.project.dto.productDto.ProductDTO;
import com.example.project.exceptions.ProductNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.repository.IOrderRepository;
import com.example.project.repository.IProductRepository;
import com.example.project.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    IProductRepository productRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Override
    public void addProduct(ProductDTO productDto, Category category) throws Exception {
        Product product = new Product();
        assertProductIsNotExistByCode(productDto.getCode());
        validateProductImage(productDto);
        product.setCode(productDto.getCode());
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setCategory(category);
        product.setPrice(productDto.getPrice());
        product.setCheckDate(new Date());
        product.setMinSales(productDto.getMinSales());
        product.setMaxSales(productDto.getMaxSales());
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductDto(Product product) {
        ProductDTO productDto = new ProductDTO();
        productDto.setCode(product.getCode());
        productDto.setDescription(product.getDescription());
        productDto.setImageURL(product.getImageURL());
        productDto.setName(product.getName());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setPrice(product.getPrice());
        productDto.setId(product.getId());
        productDto.setMinSales(product.getMinSales());
        productDto.setMaxSales(product.getMaxSales());
        return productDto;
    }

    @Override
    public Product getProductByCode(String code) {
        return productRepository.findProductByCode(code)
                .orElseThrow(() -> new ProductNotExistsException("product code is invalid: " + code));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product product : allProducts) {
            productDTOS.add(getProductDto(product));
        }
        return productDTOS;
    }


    @Override
    public void updateProduct(ProductDTO productDto) throws Exception {
        Product product = productRepository.findProductByCode(productDto.getCode())
                .orElse(null);

        assertProductIsNotNull(product);
        validateProductImage(productDto);

        product.setCode(productDto.getCode());
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setMinSales(productDto.getMinSales());
        product.setMaxSales(productDto.getMaxSales());
        productRepository.save(product);
    }

    private void validateProductImage(ProductDTO productDto) throws Exception {
        if (productDto.getImageURL().length() > 240) {
            throw new Exception("Image URL is too long!");
        }
    }

    @Override
    public Product findProductById(Long productId) throws ProductNotExistsException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotExistsException("product id is invalid: " + productId));
        return product;
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    @Override
    public Date convertDate() {
        Date date = convertToDateViaSqlDate(convertToLocalDateViaInstant(new Date()).minusMonths(1).minusDays(1));
        return date;
    }

    @Override
    public void checkPrices() {
        List<Product> products = productRepository.findAll();

        Date todayDate = new Date();
        for(Product product: products){
            List<Order> orders = orderRepository.findAllByCreatedDateBetween(product.getCheckDate(), todayDate);
            double count = 0;
            for (var order : orders) {
                for (var orderUnit : order.getOrderUnits()) {
                    if (orderUnit.getProduct().getId() == product.getId()) {
                        count += orderUnit.getQuantity();
                    }
                }
            }
            if (count > product.getMaxSales()) {
                product.setPrice(Math.ceil(product.getPrice() * 1.1));
            } else if (count < product.getMinSales()) {
                product.setPrice(Math.ceil(product.getPrice() / 1.1));
            }
            product.setCheckDate(todayDate);
            productRepository.save(product);
        }

    }

    private void assertProductIsNotExistByCode(String productCode) throws IllegalArgumentException {
        if (productRepository.findProductByCode(productCode)
                .orElse(null) != null) {
            throw new IllegalArgumentException("Product with the same code has already existed!");
        }
    }

    private void assertProductIsNotNull(Product product) throws IllegalArgumentException {
        if (product != null) {
            throw new IllegalArgumentException("Product is not null!");
        }
    }

}
