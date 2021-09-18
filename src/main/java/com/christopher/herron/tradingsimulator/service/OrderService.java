package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class OrderService {

    private final String USER = "CHR";
    private final OrderBookService orderBookService;
    private final MatchingEngineService matchingEngineService;
    private final UserService userService;


    @Autowired
    public OrderService(OrderBookService orderBookService, MatchingEngineService matchingEngineService, UserService userService) {
        this.orderBookService = orderBookService;
        this.matchingEngineService = matchingEngineService;
        this.userService = userService;
    }

    public void addOrderEntryTEMPCHRILLE(@ModelAttribute Order order) {
        order.setUserId(USER);
        order.setOrderId(orderBookService.generateOrderId());
        addOrder(order);
    }

    public void addOrder(@ModelAttribute Order order) {
        userService.addOrderToUser(order);
        orderBookService.addOrderToOrderBook(order);

        matchingEngineService.runMatchingEngine();
    }
}
