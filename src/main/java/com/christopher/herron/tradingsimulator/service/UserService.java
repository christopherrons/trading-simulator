package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserService {

    private final UserCache userCache;
    private final OrderBookService orderBookService;

    @Autowired
    public UserService(UserCache userCache, OrderBookService orderBookService) {
        this.userCache = userCache;
        this.orderBookService = orderBookService;
    }

    public List<Order> getUserOpenOrders(final String userId) {
        return getUserOrders(userId, OrderStatusEnum.OPEN);
    }

    public List<Order> getUserFilledOrders(final String userId) {
        return getUserOrders(userId, OrderStatusEnum.FILLED);
    }

    private List<Order> getUserOrders(final String userId, final OrderStatusEnum orderStatus) {
        List<Order> orders = orderBookService.getUserOrders(userId, orderStatus);
        return orders == null ? Collections.emptyList() : orders;
    }

    public int generateUserId() {
        return userCache.generateUserId();
    }

}
