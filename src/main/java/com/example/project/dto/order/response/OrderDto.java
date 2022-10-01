package com.example.project.dto.order.response;


import java.util.List;

public class OrderDto {

    private List<OrderDtoItem> orderDtoItems;
    private double totalCost;

    public OrderDto() {
    }

    public List<OrderDtoItem> getOrderItems() {
        return orderDtoItems;
    }

    public void setOrderItems(List<OrderDtoItem> orderDtoItems) {
        this.orderDtoItems = orderDtoItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
