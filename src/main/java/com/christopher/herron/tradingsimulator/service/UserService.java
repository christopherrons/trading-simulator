package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class UserService {

    private final UserCache userCache;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    public UserService(UserCache userCache) {
        this.userCache = userCache;
    }

    public List<Order> getUserOpenOrders(final String userId) {
        User user = userCache.getUser(userId);
        return user == null ? Collections.emptyList() : user.getOpenOrders();
    }

    public void updateUserOrderStatus(final String userId, final long orderId, final OrderStatusEnum currentOrderStatus, final OrderStatusEnum newOrderStatus) {
        userCache.updateUserOrderStatus(userId, orderId, currentOrderStatus, newOrderStatus);
    }

    public void addUserOrder(Order order) {
        readWriteLock.writeLock().lock();
        try {
            userCache.addUserOrder(order);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public int generateUserId() {
        return userCache.generateUserId();
    }

}
