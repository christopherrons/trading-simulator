package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserCache {

    public Map<String, User> userIdToUser = new ConcurrentHashMap<>();

    public User getUser(final String userId) {
        return userIdToUser.get(userId);
    }

    public void addUserOrder(final Order order) {
        userIdToUser.computeIfAbsent(order.getUserId(), User::new).addOrder(order);
    }

    public void updateUserOrderStatus(final String userId, final long orderId, final OrderStatusEnum currentOrderStatus, final OrderStatusEnum newOrderStatus) {
        userIdToUser.get(userId).updateUserOrderStatus(orderId, currentOrderStatus, newOrderStatus);
    }

    public int generateUserId() {
        return userIdToUser.keySet().size() + 1;
    }
}
