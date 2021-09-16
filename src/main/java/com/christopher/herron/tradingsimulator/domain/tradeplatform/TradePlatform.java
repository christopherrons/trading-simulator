package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.domain.transactions.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradePlatform {

    private final OrderBook orderBook;
    private final UserCache userCache;
    private final List<Trade> trades = new ArrayList<>();

    @Autowired
    public TradePlatform(OrderBook orderBook, UserCache userCache) {
        this.orderBook = orderBook;
        this.userCache = userCache;
    }

    public List<Double> getVolatility() {
        List<Trade> trades = getTrades();
        List<Double> returns = new ArrayList<>();
        for (int i = 0; i < trades.size() - 1; i++) {
            returns.add((double) trades.get(i + 1).getPrice() / (double) trades.get(i).getPrice());
        }
        return returns;
    }

    public void addOrder(final Order order) {
        orderBook.addOrder(order);
    }

    public long getTotalNrOfOrdersEntered() {
        return orderBook.getTotalNrOfOrdersEntered();
    }

    public List<Order> getBuyOrders() {
        return orderBook.getBuyOrders();
    }

    public List<Order> getSellOrders() {
        return orderBook.getSellOrders();
    }

    public void addUserOrder(Order order) {
        userCache.addUserOrder(order);
    }

    public int getTotalNrOfUsers() {
        return userCache.getTotalNrOfUsers();
    }

    public void addTrade(final Trade trade) {
        this.trades.add(0, trade);
    }

    public long getLatestTradesPrice() {
        return !trades.isEmpty() ? trades.get(0).getPrice() : 100L;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}
