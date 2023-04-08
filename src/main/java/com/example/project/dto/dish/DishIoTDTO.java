package com.example.project.dto.dish;

import javax.validation.constraints.NotNull;

public class DishIoTDTO {

    private Long id;
    private @NotNull String searchId;
    private @NotNull String name;
    private @NotNull double price;
    private @NotNull String description;

    public DishIoTDTO() {
    }

    public DishIoTDTO(Long id, String searchId, String name, double price, String description) {
        this.id = id;
        this.searchId = searchId;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
