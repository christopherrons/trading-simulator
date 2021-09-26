package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.User;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class OrderService {

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

    public void addOrderEntryFromUser(Order order) {
        userService.addUser(new User(SimulationUtils.getSimulationUser()));

        order.setUserId(SimulationUtils.getSimulationUser());
        order.setInstrumentId(SimulationUtils.getSimulationInstrumentId());
        order.setOrderId(orderBookService.generateOrderId());
        addOrder(order);
    }

    public void addOrder(Order order) {
        readWriteLock.writeLock().lock();
        try {
            if (order.getUserId().equals(SimulationUtils.getSimulationUser())) {
                userService.updateUserOrderTableView(order);
            }

            orderBookService.addOrderToOrderBook(order);
        } finally {
            readWriteLock.writeLock().unlock();
        }

        matchingEngineService.runMatchingEngine(SimulationUtils.getSimulationInstrumentId());
    }
}
