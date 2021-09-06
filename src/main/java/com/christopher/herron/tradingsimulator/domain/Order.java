package com.christopher.herron.tradingsimulator.domain;

public class Order {

    private long quantity;
    private short orderType;
    private long orderId = 1;
    private long price;

    public Order() {}

    public short getOrderType() {
        return orderType;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setOrderType(short orderType) {
        this.orderType = orderType;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = 1L;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
