package com.example.core;

import java.io.Serializable;

public class CryptoPriceDTO implements Serializable
{
    private String symbol;
    private Double price;

    public CryptoPriceDTO() {}

    public CryptoPriceDTO(String symbol, Double price)
    {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}