package com.example.project.dto.liqpay;


public class PayOptions {

    private String version;
    private String amount;
    private String action;
    private String currency;
    private String description;
    private String orderId;

    public PayOptions() {
    }

    public PayOptions(String version, String amount, String action, String currency, String description, String orderId) {
        this.version = version;
        this.amount = amount;
        this.action = action;
        this.currency = currency;
        this.description = description;
        this.orderId = orderId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
