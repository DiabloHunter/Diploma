package com.example.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "OrderProduct")
@Table(name = "orders_products")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @JsonIgnore
    @MapsId("orderId")
    private Order order;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnore
    @MapsId("productId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(name = "quantity")
    private double quantity;

    public OrderProduct() {
    }

    public OrderProduct(Order order, Product product, int quantity) {
        this.id = new OrderProductId(order.getId(), product.getId());
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProductId getId() {
        return id;
    }

    public void setId(OrderProductId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return quantity == that.quantity && Objects.equals(order, that.order) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product, quantity);
    }
}
