package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.tradeengine.OrderBook;
import com.christopher.herron.tradingsimulator.view.OrderBookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookService {

    private final OrderBook orderBook;
    private final OrderBookView orderBookView;

    @Autowired
    public OrderBookService(OrderBook orderBook, OrderBookView orderBookView) {
        this.orderBook = orderBook;
        this.orderBookView = orderBookView;
    }

    public void addOrderToOrderBook(final Order order) {
        orderBook.addOrderToOrderBook(order);
        updateOrderBookTableView(order);
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder, long tradeQuantity) {
        orderBook.updateOrderBookAfterTrade(buyOrder, sellOrder, tradeQuantity);
        updateOrderBookViewAfterTrade(buyOrder, sellOrder);
    }

    public void updateOrderBookTableView(final Order order) {
        orderBookView.updateOrderBook(order);
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder) {
        orderBookView.updateOrderBookViewAfterTrade(buyOrder, sellOrder);
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
