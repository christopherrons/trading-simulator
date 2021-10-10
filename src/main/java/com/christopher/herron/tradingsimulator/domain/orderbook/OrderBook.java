package com.christopher.herron.tradingsimulator.domain.orderbook;

import com.christopher.herron.tradingsimulator.common.enumerators.MatchingAlgorithmEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;

public abstract class OrderBook implements ReadOnlyOrderBook {
    public static OrderBook createOrderBook(final String instrumentId, final String matchingAlgorithm) {
        switch (MatchingAlgorithmEnum.fromValue(matchingAlgorithm)) {
            case FIFO:
            default:
                return new FifoOrderBook(instrumentId);
        }
    }

    public abstract void addOrder(final Order order);

    public abstract void removeOrder(final long orderId);
}
