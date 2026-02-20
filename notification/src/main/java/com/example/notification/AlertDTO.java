package com.example.notification;

import java.io.Serializable;

public class AlertDTO implements Serializable
{
    private String message;
    private Double price;

    public AlertDTO() {}

    public AlertDTO(String message, Double price) {
        this.message = message;
        this.price = price;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
