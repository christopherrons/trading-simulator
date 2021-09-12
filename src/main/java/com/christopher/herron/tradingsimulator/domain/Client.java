package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Client {

    private final String clientId;
    private final Map<OrderStatusEnum, Map<Long, Order>> orderStatusToOrder = new LinkedHashMap<>();

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public Collection<Order> getClientOrders(OrderStatusEnum orderStatus) {
        return orderStatusToOrder.computeIfAbsent(orderStatus, value -> new LinkedHashMap<>()).values();
    }

    public void addOrder(Order order) {
        orderStatusToOrder.computeIfAbsent(OrderStatusEnum.fromValue(order.getOrderStatus()), value -> new LinkedHashMap<>()).put(order.getOrderId(), order);
    }

    public void updateClientOrderStatus(long orderId, OrderStatusEnum currentOrderStatus, OrderStatusEnum newOrderStatus) {
        Order order = orderStatusToOrder.get(currentOrderStatus).remove(orderId);
        order.setOrderStatus(newOrderStatus.getValue());
        addOrder(order);
    }
}
