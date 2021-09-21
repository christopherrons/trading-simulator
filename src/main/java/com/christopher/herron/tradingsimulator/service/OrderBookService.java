package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.cache.OrderBookCache;
import com.christopher.herron.tradingsimulator.view.OrderBookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookService {

    private final OrderBookCache orderBookCache;
    private final OrderBookView orderBookView;

    @Autowired
    public OrderBookService(OrderBookCache orderBookCache, OrderBookView orderBookView) {
        this.orderBookCache = orderBookCache;
        this.orderBookView = orderBookView;
    }

    public void addOrderToOrderBook(final Order order) {
        orderBookCache.addOrderToOrderBook(order);
        updateOrderBookTableView(order);
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, long tradeQuantity) {
        orderBookCache.updateOrderBookAfterTrade(buyOrder, sellOrder, tradeQuantity);
        updateOrderBookViewAfterTrade(buyOrder, sellOrder);
    }

    public void updateOrderBookTableView(final Order order) {
        orderBookView.updateOrderBook(order);
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder) {
        orderBookView.updateOrderBookViewAfterTrade(buyOrder, sellOrder);
    }

    public Order getBestBuyOrder() {
        return orderBookCache.getBestBuyOrder();
    }

    public Order getBestSellOrder() {
        return orderBookCache.getBestSellOrder();
    }

    public List<Order> getBuyOrders() {
        return orderBookCache.getBuyOrders();
    }

    public List<Order> getSellOrders() {
        return orderBookCache.getSellOrders();
    }

    public List<Order> getUserOrders(final String userId, final OrderStatusEnum orderStatus) {
        return orderBookCache.getUserOrders(userId, orderStatus);
    }

    public long generateOrderId() {
        return orderBookCache.generateOrderId();
    }

    public long getTotalNumberOfOrders() {
        return orderBookCache.getTotalNumberOfOrders();
    }
}
