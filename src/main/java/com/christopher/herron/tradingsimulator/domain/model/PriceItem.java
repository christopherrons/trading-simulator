package com.christopher.herron.tradingsimulator.domain.model;

public class PriceItem {

    private final double price;
    private final short priceType;

    public PriceItem(long price, short priceType) {
        this.price = price;
        this.priceType = priceType;
    }

    public double getPrice() {
        return price;
    }

    public short getPriceType() {
        return priceType;
    }
}
