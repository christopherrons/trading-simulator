package com.christopher.herron.tradingsimulator.config;

import com.christopher.herron.tradingsimulator.common.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.controller.OrderController;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableScheduling
@Configuration
public class Scheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final int MAX_USER_ORDERS_IN_TABLE = 10;
    private final int MAX_TRADES_IN_TABLE = 10;
    private final int MAX_ORDERBOOK_ORDERS_IN_TABLE = 5;
    private final String USER = "CHR";

    @Autowired
    public Scheduler(SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @Scheduled(fixedRate = 20000)
    public void updateOpenOrderView() {
        List<Order> openOrders = userService.getOpenUserOrders(USER);
        Collections.reverse(openOrders);
        openOrders = openOrders.size() > MAX_USER_ORDERS_IN_TABLE ? openOrders.subList(openOrders.size() - MAX_USER_ORDERS_IN_TABLE, openOrders.size()) : openOrders;
        OrderController.Greeting greeting = new OrderController.Greeting("Hello, " + "!", "test");
        OrderController.Greeting greetingTwo = new OrderController.Greeting("Hello, " + "!", "TWO");
        List<OrderController.Greeting> hej = new ArrayList<>();
        hej.add(greeting);
        hej.add(greetingTwo);
        DataTableWrapper<OrderController.Greeting> temp = new DataTableWrapper<>();
        temp.addData(hej);
        messagingTemplate.convertAndSend("/topic/greetings", new DataTableWrapper<>(hej));
        messagingTemplate.convertAndSend("/topic/openOrders", new DataTableWrapper<>(openOrders));
    }
}
