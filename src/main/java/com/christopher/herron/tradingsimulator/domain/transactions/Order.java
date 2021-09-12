package com.christopher.herron.tradingsimulator.domain.transactions;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;

import java.time.Instant;

public class Order implements Comparable<Order> {

    private long initialQuantity;
    private long currentQuantity;
    private short orderType;
    private long orderId;
    private long price;
    private Instant timeStamp = Instant.now();
    private String userId;
    private short orderStatus = OrderStatusEnum.OPEN.getValue();


    public Order() {
    }

    public static Order valueOf(long orderId, String userId, short orderStatus, Instant timeStamp, long quantity, long price, short orderType) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setOrderStatus(orderStatus);
        order.setTimeStamp(timeStamp);
        order.setCurrentQuantity(quantity);
        order.setInitialQuantity(quantity);
        order.setPrice(price);
        order.setOrderType(orderType);
        return order;
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

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void updateQuantity(long quantityTraded) {
        this.currentQuantity = this.currentQuantity - quantityTraded;
    }

    public boolean isOrderFilled() {
        return this.currentQuantity == 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
