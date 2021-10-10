package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.Instrument;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.OrderBook;
import com.christopher.herron.tradingsimulator.domain.model.ReadOnlyOrderBook;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookCache {

    private final Map<String, OrderBook> instrumentIdToOrderBook = new ConcurrentHashMap<>();
    private long totalNumberOfOrders;

    public OrderBookCache() {
    }

    public void addOrderToOrderBook(final Order order, final Instrument instrument) {
        instrumentIdToOrderBook.computeIfAbsent(instrument.getInstrumentId(), key -> OrderBook.createOrderBook(key, instrument.getMatchingAlgorithm()))
                .addOrder(order);

        totalNumberOfOrders++;
    }

    public void removeOrderFromOrderBook(final Order order) {
        instrumentIdToOrderBook.get(order.getInstrumentId()).removeOrder(order.getOrderId());
    }

    public ReadOnlyOrderBook getOrderBook(final String instrumentId) {
        return instrumentIdToOrderBook.get(instrumentId);
    }

    public long generateOrderId() {
        return totalNumberOfOrders + 1;
    }

    public long getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }
}
