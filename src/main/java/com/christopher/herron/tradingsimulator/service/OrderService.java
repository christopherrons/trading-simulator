package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class OrderService {

    private final String USER = "CHR";
    private final OrderBookService orderBookService;
    private final MatchingEngineService matchingEngineService;
    private final UserService userService;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    @Autowired
    public OrderService(OrderBookService orderBookService, MatchingEngineService matchingEngineService, UserService userService) {
        this.orderBookService = orderBookService;
        this.matchingEngineService = matchingEngineService;
        this.userService = userService;
    }

    public void addOrderEntryTEMPCHRILLE(Order order) { //TODO: Fix this
        order.setUserId(USER);
        order.setOrderId(orderBookService.generateOrderId());
        addOrder(order);
    }

    public void addOrder(Order order) {
        readWriteLock.writeLock().lock();
        try {
            orderBookService.addOrderToOrderBook(order);
            if (order.getUserId().equals(USER)) {
                userService.updateUserOrderTableView(order); //TODO: Fix this
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

        matchingEngineService.runMatchingEngine();
    }
}
