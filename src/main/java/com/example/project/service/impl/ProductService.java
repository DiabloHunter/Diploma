package com.example.project.service.impl;

import com.example.project.dto.productDto.ProductDto;
import com.example.project.exceptions.ProductNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.repository.IOrderRepository;
import com.example.project.repository.IProductRepository;
import com.example.project.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    IProductRepository IProductRepository;

    @Autowired
    IOrderRepository IOrderRepository;

    @Override
    public void addProduct(ProductDto productDto, Category category) throws Exception {
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
        IProductRepository.save(product);
    }

    @Override
    public ProductDto getProductDto(Product product) {
        ProductDto productDto = new ProductDto();
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
        return IProductRepository.findProductByCode(code)
                .orElseThrow(() -> new ProductNotExistsException("product code is invalid: " + code));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = IProductRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        boolean isChanged = false;
        for (Product product : allProducts) {
            Date todayDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            boolean isTimeToCorrectPrice = false;
            try {
                Date parsedDate1 = sdf.parse(sdf.format(product.getCheckDate()));
                Date parsedDate2 = sdf.parse(sdf.format(todayDate));

                LocalDateTime oldDate = parsedDate1.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime newDate = parsedDate2.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                if (oldDate.plusMonths(1).isBefore(newDate) || oldDate.plusMonths(1).equals(newDate)) {
                    isTimeToCorrectPrice = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (isTimeToCorrectPrice) {
                List<Order> orders = IOrderRepository.findAllByCreatedDateBetween(product.getCheckDate(), todayDate);
                double count = 0;
                for (var order : orders) {
//                    for(var productInOrder:order.getOrderProducts()){
//                        if( productInOrder.getId().getProductId()==product.getId()){
//                            count+=productInOrder.getQuantity();
//                        }
//                    }
                }
                if (count > product.getMaxSales()) {
                    product.setPrice(Math.ceil(product.getPrice() * 1.1));
                    isChanged = true;
                } else if (count < product.getMinSales()) {
                    product.setPrice(Math.ceil(product.getPrice() / 1.1));
                    isChanged = true;
                }
            }
            if (isChanged) {
                product.setCheckDate(todayDate);
                IProductRepository.save(product);
            }

            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }


    @Override
    public void updateProduct(ProductDto productDto) throws Exception {
        Product product = IProductRepository.findProductByCode(productDto.getCode())
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
        IProductRepository.save(product);
    }

    private void validateProductImage(ProductDto productDto) throws Exception {
        if (productDto.getImageURL().length() > 240) {
            throw new Exception("Image URL is too long!");
        }
    }

    @Override
    public Product findProductById(Long productId) throws ProductNotExistsException {
        Product product = IProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotExistsException("product id is invalid: " + productId));
        return product;
    }

    @Override
    public void deleteProductById(Long productId) {
        IProductRepository.deleteById(productId);
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
    public void checkPrices(Date change) {
        List<Product> products = IProductRepository.findAll();
        for (var el : products) {
            el.setCheckDate(change);
            IProductRepository.save(el);
        }
    }

    private void assertProductIsNotExistByCode(String productCode) throws IllegalArgumentException {
        if (IProductRepository.findProductByCode(productCode)
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
