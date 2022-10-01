package com.example.project.dto.order.response;

import javax.validation.constraints.NotNull;
import java.util.*;

public class OrderDtoItem {

    private Long orderId;
    private @NotNull Double price;
    private @NotNull List<OrderProductDto> products = new ArrayList<>();
    private @NotNull Long userId;
    private @NotNull Date createdDate;

    public OrderDtoItem() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDto> products) {
        this.products = products;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
