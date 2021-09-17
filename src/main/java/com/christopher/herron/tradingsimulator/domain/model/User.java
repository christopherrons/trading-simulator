package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {

    private final String userId;
    private final Map<OrderStatusEnum, Map<Long, Order>> orderStatusToOrder = new ConcurrentHashMap<>();

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Order> getOpenOrders() {
        return new ArrayList<>(orderStatusToOrder.computeIfAbsent(OrderStatusEnum.OPEN, key -> new ConcurrentHashMap<>()).values());
    }

    public void addOrder(Order order) {
        orderStatusToOrder.computeIfAbsent(OrderStatusEnum.fromValue(order.getOrderStatus()), key -> new ConcurrentHashMap<>()).put(order.getOrderId(), order);
    }

    public void updateUserOrderStatus(long orderId, OrderStatusEnum currentOrderStatus, OrderStatusEnum newOrderStatus) {
        Order order = orderStatusToOrder.get(currentOrderStatus).remove(orderId);
        order.setOrderStatus(newOrderStatus.getValue());
        addOrder(order);
    }
}
