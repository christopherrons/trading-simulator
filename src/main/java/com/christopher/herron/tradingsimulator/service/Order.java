package com.christopher.herron.tradingsimulator.service;

public class Order {

    long quantity;
    short orderType;
    long orderID;
    PriceItem priceItem;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public short getOrderType() {
        return orderType;
    }

    public void setOrderType(short orderType) {
        this.orderType = orderType;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public PriceItem getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(PriceItem priceItem) {
        priceItem = new PriceItem(100L, 2, (short) 1);
        this.priceItem = priceItem;
    }

}
