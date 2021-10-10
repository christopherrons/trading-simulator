package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.MatchingAlgorithmEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class FifoOrderBook extends OrderBook {

    private final Map<Long, OrderBookEntry> orderIdToOrderBookEntry = new ConcurrentHashMap<>();
    private final TreeMap<Long, Limit> priceToBuyLimits = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, Limit> priceToSellLimits = new TreeMap<>();
    private final MatchingAlgorithmEnum matchingAlgorithm = MatchingAlgorithmEnum.FIFO;
    private final String instrumentId;

    public FifoOrderBook(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    @Override
    public void addOrder(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                Limit limit = priceToBuyLimits.computeIfAbsent(order.getPrice(), Limit::new);
                OrderBookEntry newOrderBookEntry = new OrderBookEntry(order, limit);
                limit.addOrderBookEntry(newOrderBookEntry);
                orderIdToOrderBookEntry.putIfAbsent(order.getOrderId(), newOrderBookEntry);
                break;

            case SELL:
                limit = priceToSellLimits.computeIfAbsent(order.getPrice(), Limit::new);
                newOrderBookEntry = new OrderBookEntry(order, limit);
                limit.addOrderBookEntry(newOrderBookEntry);
                orderIdToOrderBookEntry.putIfAbsent(order.getOrderId(), newOrderBookEntry);
                break;

            default:
                break;
        }
    }

    @Override
    public void removeOrder(final long orderId) {
        removeOrder(orderIdToOrderBookEntry.get(orderId));
        orderIdToOrderBookEntry.remove(orderId);
    }

    private void removeOrder(final OrderBookEntry orderBookEntry) {
        updateOrderBookEntry(orderBookEntry);
        updateLimit(orderBookEntry);
    }

    private void updateOrderBookEntry(final OrderBookEntry orderBookEntry) {
        if (orderBookEntry.isInMiddle()) {
            orderBookEntry.next.previous = orderBookEntry.previous;
            orderBookEntry.previous.next = orderBookEntry.next;
        } else if (orderBookEntry.isOnlyTail()) {
            orderBookEntry.previous.next = null;
        } else if (orderBookEntry.isOnlyHead()) {
            orderBookEntry.next.previous = null;
        }
    }

    private void updateLimit(final OrderBookEntry orderBookEntry) {
        Limit limit = orderBookEntry.getLimit();
        if (orderBookEntry.isHeadAndTails()) {
            switch (OrderTypeEnum.fromValue(orderBookEntry.getOrder().getOrderType())) {
                case BUY:
                    priceToBuyLimits.remove(limit.getPrice());
                    break;
                case SELL:
                    priceToSellLimits.remove(limit.getPrice());
                    break;
            }
        } else if (orderBookEntry.isOnlyHead()) {
            limit.head = orderBookEntry.next;

        } else if (orderBookEntry.isOnlyTail()) {
            limit.tail = orderBookEntry.previous;
        }
    }

    public Collection<Limit> getBuyOrderBookEntries() {
        return priceToBuyLimits.values();
    }

    public Collection<Limit> getSellOrderBookEntries() {
        return priceToSellLimits.values();
    }

    public MatchingAlgorithmEnum getMatchingAlgorithm() {
        return matchingAlgorithm;
    }
}
