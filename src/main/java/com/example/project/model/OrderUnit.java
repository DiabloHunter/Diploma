package com.example.project.model;

import javax.persistence.*;

@Entity
@Table(name = "orderUnit")
public class OrderUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapKey(name = "id")
    private Product product;

    private Double quantity;

    public OrderUnit(Product product, Double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderUnit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
