package com.example.project.dto.order.response;


import java.util.List;

public class OrderDTO {

    private List<OrderItemDTO> orderItemDTOS;
    private double totalCost;

    public OrderDTO() {
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItemDTOS;
    }

    public void setOrderItems(List<OrderItemDTO> orderItemDTOS) {
        this.orderItemDTOS = orderItemDTOS;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
