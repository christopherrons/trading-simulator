package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.domain.utils.MatchingEngineUtils;

import java.util.Date;

public class Order implements Comparable<Order> {

    private final MatchingEngineUtils matchingEngineUtils = new MatchingEngineUtils();
    private long quantity;
    private short orderType;
    private long orderId = matchingEngineUtils.generateOrderId();
    private long price;
    private Date timeStamp = new Date();


    public Order() {
    }

    public short getOrderType() {
        return orderType;
    }

    public void setOrderType(short orderType) {
        this.orderType = orderType;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void updateQuantity(long quantityTraded) {
        this.quantity = this.quantity - quantityTraded;
    }

    public boolean isOrderFilled() {
        return this.quantity == 0;
    }

    @Override
    public int compareTo(Order otherOrder) {
        return Long.compare(this.getPrice(), otherOrder.getPrice());
    }
}
