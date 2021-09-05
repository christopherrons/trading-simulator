package com.christopher.herron.tradingsimulator.service;

public class PriceItem {

    private final long price;
    private final int decimalsInPrice;
    private final short priceType;

    public PriceItem(long price, int decimalsInPrice, short priceType) {
        this.price = price;
        this.decimalsInPrice = decimalsInPrice;
        this.priceType = priceType;
    }

    public long getPrice() {
        return price;
    }

    public int getDecimalsInPrice() {
        return decimalsInPrice;
    }

    public short getPriceType() {
        return priceType;
    }
}
