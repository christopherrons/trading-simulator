package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.User;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderBookService orderBookService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderBookService orderBookService, UserService userService) {
        this.orderBookService = orderBookService;
        this.userService = userService;
    }

    public void addOrderEntryFromUser(final Order order) {
        userService.addUser(new User(SimulationUtils.getSimulationUser()));

        order.setUserId(SimulationUtils.getSimulationUser());
        order.setInstrumentId(SimulationUtils.getSimulationInstrumentId());
        order.setOrderId(orderBookService.generateOrderId());
        addOrder(order);
    }

    public void addOrder(final Order order) {
        if (order.getUserId().equals(SimulationUtils.getSimulationUser())) {
            userService.updateUserOrderTableView(order);
        }

        orderBookService.writeToOrderBook(order);
    }
}
