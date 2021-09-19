package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.tradeengine.OrderBook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class OrderBookService {

    private final OrderBook orderBook;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public OrderBookService(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void addOrderToOrderBook(final Order order) {
        orderBook.addOrderToOrderBook(order);
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, long tradeQuantity) {
        orderBook.updateOrderBookAfterTrade(buyOrder, sellOrder, tradeQuantity);
    }

    public Order getBestBuyOrder() {
        return orderBook.getBestBuyOrder();
    }

    public Order getBestSellOrder() {
        return orderBook.getBestSellOrder();
    }

    public List<Order> getBuyOrders() {
        return orderBook.getBuyOrders();
    }

    public List<Order> getSellOrders() {
        return orderBook.getSellOrders();
    }

    public List<Order> getUserOrders(final String userId, final OrderStatusEnum orderStatus) {
        return orderBook.getUserOrders(userId, orderStatus);
    }

    public long generateOrderId() {
        return orderBook.generateOrderId();
    }
}
