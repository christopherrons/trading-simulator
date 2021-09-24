package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    private final UserCache userCache;
    private final OrderBookService orderBookService;
    private final UserView userView;

    @Autowired
    public UserService(UserCache userCache, OrderBookService orderBookService, UserView userView) {
        this.userCache = userCache;
        this.orderBookService = orderBookService;
        this.userView = userView;
    }

    public void updateUserOrderTableView(Order order) {
        switch (OrderStatusEnum.fromValue(order.getOrderStatus())) {
            case OPEN:
                userView.updateOpenOrderTableView(order);
                break;
            case FILLED:
                userView.updateFilledOrderTableView(order);
                break;
        }
    }

    public List<Order> getUserOpenOrders(final String userId) {
        return getUserOrders(userId, OrderStatusEnum.OPEN);
    }

    public List<Order> getUserFilledOrders(final String userId) {
        return getUserOrders(userId, OrderStatusEnum.FILLED);
    }

    private List<Order> getUserOrders(final String userId, final OrderStatusEnum orderStatus) {
        return orderBookService.getUserOrders(userId, orderStatus);
    }


    public int generateUserId() {
        return userCache.generateUserId();
    }

}
