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

    public User getUser(final String userId) {
        return userIdToUser.get(userId);
    }

    public void addUserOrder(final Order order) {
        userIdToUser.computeIfAbsent(order.getUserId(), User::new).addOrder(order);
    }

    public void updateUserOrderStatus(final String userId, final long orderId, final OrderStatusEnum currentOrderStatus, final OrderStatusEnum newOrderStatus) {
        userIdToUser.get(userId).updateUserOrderStatus(orderId, currentOrderStatus, newOrderStatus);
    }

    public int getTotalNrOfUsers() {
        return userIdToUser.keySet().size();
    }
}
