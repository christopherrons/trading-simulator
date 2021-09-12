package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserCache {

    public Map<String, User> userIdToUser = new LinkedHashMap<>();

    public List<Order> getNUserOrders(int maxUserOrders, String userId, OrderStatusEnum orderStatus) {
        List<Order> orders = userIdToUser.get(userId).getUserOrders(orderStatus);
        Collections.reverse(orders);
        return orders.size() > maxUserOrders ? orders.subList(orders.size() - maxUserOrders, orders.size()) : orders;
    }

    public void addUserOrder(Order order) {
        userIdToUser.computeIfAbsent(order.getUserId(), value -> new User(order.getUserId())).addOrder(order);
    }

    public void updateUserOrderStatus(String userId, long orderId, OrderStatusEnum currentOrderStatus, OrderStatusEnum newOrderStatus) {
        userIdToUser.get(userId).updateUserOrderStatus(orderId, currentOrderStatus, newOrderStatus);
    }

    public int getTotalNrOfUsers() {
        return userIdToUser.keySet().size();
    }
}
