package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradePlatform;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserService {

    private final TradePlatform tradePlatform;
    private final UserCache userCache;

    @Autowired
    public UserService(TradePlatform tradePlatform, UserCache userCache) {
        this.tradePlatform = tradePlatform;
        this.userCache = userCache;
    }

    public List<Order> getOpenUserOrders(String userId) {
        User user = userCache.getUser(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return userCache.getUser(userId).getUserOpenOrders();
    }
}
