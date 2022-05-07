package com.example.project.dto.order;

import com.example.project.dto.ProductDto;

import javax.validation.constraints.NotNull;
import java.util.*;

public class OrderDtoItem {

    private int id;
    private @NotNull double price;
    private @NotNull List<OrderProductDto> products = new ArrayList<>();
    private @NotNull int userId;
    private @NotNull Date createdDate;

    public OrderDtoItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDto> products) {
        this.products = products;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void addProduct(OrderProductDto orderProductDto){
        products.add(orderProductDto);
    }
}
