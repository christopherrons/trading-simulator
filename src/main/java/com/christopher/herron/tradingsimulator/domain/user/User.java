package com.christopher.herron.tradingsimulator.domain.user;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class User {

    private final String userId;
    private final Map<OrderStatusEnum, Map<Long, Order>> orderStatusToOrder = new LinkedHashMap<>();

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Order> getUserOpenOrders() {
        return new ArrayList<>(orderStatusToOrder.computeIfAbsent(OrderStatusEnum.OPEN, key -> new LinkedHashMap<>()).values());
    }

    public void addOrder(Order order) {
        orderStatusToOrder.computeIfAbsent(OrderStatusEnum.fromValue(order.getOrderStatus()), key -> new LinkedHashMap<>()).put(order.getOrderId(), order);
    }

    public void updateUserOrderStatus(long orderId, OrderStatusEnum currentOrderStatus, OrderStatusEnum newOrderStatus) {
        Order order = orderStatusToOrder.get(currentOrderStatus).remove(orderId);
        order.setOrderStatus(newOrderStatus.getValue());
        addOrder(order);
    }
}
