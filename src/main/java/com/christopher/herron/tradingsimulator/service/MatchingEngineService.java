package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.MatchingEngine;
import com.christopher.herron.tradingsimulator.domain.Order;
import com.christopher.herron.tradingsimulator.domain.OrderBook;
import com.christopher.herron.tradingsimulator.domain.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchingEngineService {

    private final OrderBook orderBook;
    private final MatchingEngine matchingEngine;

    @Autowired
    public MatchingEngineService(final OrderBook orderBook, final MatchingEngine matchingEngine) {
        this.orderBook = orderBook;
        this.matchingEngine = matchingEngine;
    }

    public void addOrder(final Order order) {
        orderBook.addOrder(order);
    }

    public List<Order> getBuyOrders() {
        return orderBook.getBuyOrders();
    }

    public List<Order> getSellOrders() {
        return orderBook.getSellOrders();
    }

    public void matchOrders() {
        matchingEngine.matchOrders(orderBook);
    }

    public List<Trade> getTrades() {
        return matchingEngine.getTrades();
    }
}
