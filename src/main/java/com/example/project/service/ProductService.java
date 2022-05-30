package com.example.project.service;

import com.example.project.dto.ProductDto;
import com.example.project.exceptions.CustomException;
import com.example.project.exceptions.ProductNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.repository.OrderRepository;
import com.example.project.repository.ProductRepository;
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
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    public void createProduct(ProductDto productDto, Category category) throws Exception {
        Product product = new Product();
        Product productInDb = productRepository.findProductByCode(productDto.getCode());
        if(productInDb!=null){
            throw new Exception("Product with the same code has already existed!");
        }
        validateProduct(productDto);
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

    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();


        boolean isChanged = false;
        for(Product product: allProducts) {
            Date todayDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            boolean isTimeToCorrectPrice=false;
            try {
                Date parsedDate1 = sdf.parse(sdf.format(product.getCheckDate()));
                Date parsedDate2 = sdf.parse(sdf.format(todayDate));

                LocalDateTime oldDate =parsedDate1.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime newDate =parsedDate2.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();


                if(oldDate.plusMonths(1).isBefore(newDate) || oldDate.plusMonths(1).equals(newDate)){
                    isTimeToCorrectPrice=true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(isTimeToCorrectPrice){
                List<Order> orders = orderRepository.findAllByCreatedDateBetween(product.getCheckDate(), todayDate);
                double count = 0;
                for(var order:orders){
                    for(var productInOrder:order.getOrderProducts()){
                        if( productInOrder.getId().getProductId()==product.getId()){
                            count+=productInOrder.getQuantity();
                        }
                    }
                }
                if(count>product.getMaxSales()){
                    product.setPrice(Math.ceil(product.getPrice()*1.1));
                    isChanged = true;
                }
                else if(count<product.getMinSales()){
                    product.setPrice(Math.ceil(product.getPrice()/1.1));
                    isChanged = true;
                }
            }
            if(isChanged){
                product.setCheckDate(todayDate);
                productRepository.save(product);
            }

            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }



    public void updateProduct(ProductDto productDto, Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.get();
        Product productInDb = productRepository.findProductByCode(productDto.getCode());
        // throw an exception if product does not exists
        if(productInDb!=null && product.getId()!=productInDb.getId()){
            throw new Exception("Product with the same code has already existed!");
        }
        if (!optionalProduct.isPresent()) {
            throw new Exception("product not present");
        }
        validateProduct(productDto);

        product.setCode(productDto.getCode());
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setMinSales(productDto.getMinSales());
        product.setMaxSales(productDto.getMaxSales());
        productRepository.save(product);
    }

    private void validateProduct(ProductDto productDto) throws Exception {
        if(productDto.getImageURL().length()>240){
            throw new Exception("Image URL is too long!");
        }
    }

    public Product findById(Integer productId) throws ProductNotExistsException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotExistsException("product id is invalid: " + productId);
        }
        return optionalProduct.get();
    }

    public void deleteProduct(int productId){
        productRepository.deleteById(productId);
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public Date convertDate(){
        Date date = convertToDateViaSqlDate(convertToLocalDateViaInstant(new Date()).minusMonths(1).minusDays(1));
        return date;
    }

    public void checkPrices(Date change){
        List<Product> products = productRepository.findAll();
        for(var el:products){
            el.setCheckDate(change);
            productRepository.save(el);
        }
    }

}
