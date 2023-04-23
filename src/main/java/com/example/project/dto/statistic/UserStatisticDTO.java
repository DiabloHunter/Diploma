package com.example.project.dto.statistic;

public class UserStatisticDTO {

    private String name;
    private String email;
    private Double ordersCount;
    private Double moneyCount;
    private Double rating;

    public UserStatisticDTO() {
    }

    public UserStatisticDTO(String name, String email, Double ordersCount, Double moneyCount, Double rating) {
        this.name = name;
        this.email = email;
        this.ordersCount = ordersCount;
        this.moneyCount = moneyCount;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Double ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Double getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(Double moneyCount) {
        this.moneyCount = moneyCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
