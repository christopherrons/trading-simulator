package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.Order;
import com.christopher.herron.tradingsimulator.domain.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {

    @Autowired
    private OrderBook orderBook;

    public void addOrder(Order order) {
        orderBook.addOrder(order);
    }

    public List<Order> getBuyOrders() {
        return orderBook.getBuyOrders();
    }

    public List<Order> getSellOrders() {
        return orderBook.getSellOrders();
    }
}
