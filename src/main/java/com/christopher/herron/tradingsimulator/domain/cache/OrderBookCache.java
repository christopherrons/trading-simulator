package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.OrderBook;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookCache {

    private final Map<String, OrderBook> instrumentIdToOrderBook = new ConcurrentHashMap<>();
    private long totalNumberOfOrders;

    public OrderBookCache() {
    }

    public void addOrderToOrderBook(final Order order) {
        instrumentIdToOrderBook.computeIfAbsent(order.getInstrumentId(), key -> new OrderBook()).addOrderToOrderBook(order);
        totalNumberOfOrders++;
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        instrumentIdToOrderBook.get(buyOrder.getInstrumentId()).updateOrderBookAfterTrade(buyOrder, sellOrder, quantityTraded);
    }

    public Order getBestBuyOrder(final String instrumentId) {
        return !instrumentIdToOrderBook.containsKey(instrumentId) ? null : instrumentIdToOrderBook.get(instrumentId).getBestBuyOrder();
    }

    public Order getBestSellOrder(final String instrumentId) {
        return !instrumentIdToOrderBook.containsKey(instrumentId) ? null : instrumentIdToOrderBook.get(instrumentId).getBestSellOrder();
    }

    public long generateOrderId() {
        return totalNumberOfOrders + 1;
    }

    public long getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }
}
