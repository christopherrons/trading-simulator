package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.MatchingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class OrderService {

    private final String USER = "CHR";
    private final OrderBookService orderBookService;
    private final MatchingEngine matchingEngine;
    private final UserService userService;


    @Autowired
    public OrderService(OrderBookService orderBookService, MatchingEngine matchingEngine, UserService userService) {
        this.orderBookService = orderBookService;
        this.matchingEngine = matchingEngine;
        this.userService = userService;
    }

    public void addOrderEntryTEMPCHRILLE(@ModelAttribute Order order) {
        order.setUserId(USER);
        addOrder(order);
    }

    public void addOrder(@ModelAttribute Order order) {
        order.setOrderId(orderBookService.generateOrderId());
        userService.addUserOrder(order);

        orderBookService.addOrder(order);
        matchingEngine.matchOrders();
    }
}
