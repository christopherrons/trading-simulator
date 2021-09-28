package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.utils.MathUtils;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Order implements Comparable<Order> {

    private final short decimalsInPrice = SimulationUtils.getDecimalsInPrice();
    private long initialQuantity;
    private long currentQuantity;
    private short orderType;
    private long orderId;
    private long price;
    private Instant timeStamp = Instant.now();
    private String userId;
    private short orderStatus = OrderStatusEnum.OPEN.getValue();
    private String instrumentId;
    private short orderAction;

    public Order() {
    }

    public static Order valueOf(long orderId, String userId, short orderStatus, Instant timeStamp, long currentQuantity, long initialQuantity, double price, short orderType, String instrumentId, short orderAction) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setOrderStatus(orderStatus);
        order.setTimeStamp(timeStamp);
        order.setCurrentQuantity(currentQuantity);
        order.setInitialQuantity(initialQuantity);
        order.setPrice(price);
        order.setOrderType(orderType);
        order.setInstrumentId(instrumentId);
        order.setOrderAction(orderAction);
        return order;
    }

    public Order copy() {
        return valueOf(this.orderId, this.userId, this.orderStatus, this.timeStamp, this.currentQuantity, this.initialQuantity, getPriceAsDouble(), this.orderType, this.instrumentId, this.orderAction);
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

    public void setPrice(double price) {
        this.price = (long) (price * MathUtils.ADDING_FACTORS[decimalsInPrice]);
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void updateCurrentQuantity(long quantityTraded) {
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

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public short getDecimalsInPrice() {
        return decimalsInPrice;
    }

    public double getPriceAsDouble() {
        return MathUtils.convertToDouble(this.price, this.decimalsInPrice);
    }

    public short getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(short orderAction) {
        this.orderAction = orderAction;
    }

    public String getTimeStampHourMiniteSecond() {
        final SimpleDateFormat formatterHourMinuteSecond = new SimpleDateFormat("HH:mm:ss");
        return formatterHourMinuteSecond.format(Date.from(this.timeStamp));
    }

    @Override
    public int compareTo(Order otherOrder) {
        return Long.compare(this.getPrice(), otherOrder.getPrice());
    }
}
