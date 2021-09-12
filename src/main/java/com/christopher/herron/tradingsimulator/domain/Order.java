package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;

import java.util.Date;

public class Order implements Comparable<Order> {

    private long initialQuantity;
    private long currentQuantity;
    private short orderType;
    private long orderId;
    private long price;
    private Date timeStamp = new Date();
    private String clientId;
    private short orderStatus = OrderStatusEnum.OPEN.getValue();


    public Order() {
    }

    public short getOrderType() {
        return orderType;
    }

    public void setOrderType(short orderType) {
        this.orderType = orderType;
    }

    public long getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(long initialQuantity) {
        this.currentQuantity = initialQuantity;
        this.initialQuantity = initialQuantity;
    }

    public long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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
        this.currentQuantity = this.currentQuantity - quantityTraded;
    }

    public boolean isOrderFilled() {
        return this.currentQuantity == 0;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(short orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public int compareTo(Order otherOrder) {
        return Long.compare(this.getPrice(), otherOrder.getPrice());
    }
}
