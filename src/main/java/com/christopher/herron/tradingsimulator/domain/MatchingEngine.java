package com.christopher.herron.tradingsimulator.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchingEngine {
    private final List<Trade> trades = new ArrayList<>();

    public void matchOrders(final OrderBook orderBook) {
        while (true) {
            Order buyOrder = orderBook.getBestBuyOrder();
            Order sellOrder = orderBook.getBestSellOrder();

            if (isOneSided(buyOrder, sellOrder)) {
                return;
            }

            if (isMatch(buyOrder.getPrice(), sellOrder.getPrice())) {
                addTrade(orderBook, buyOrder, sellOrder);

            } else {
                break;
            }
        }
    }

    private boolean isMatch(final long buyPrice, final long sellPrice) {
        return buyPrice >= sellPrice;
    }

    private void addTrade(final OrderBook orderBook, final Order buyOrder, final Order sellOrder) {
        Trade trade = createTrade(buyOrder, sellOrder);
        trades.add(trade);
        updateOrderBookAfterTrade(orderBook, buyOrder, sellOrder, trade.getQuantity());
    }
    private Trade createTrade(final Order buyOrder, final Order sellOrder) {
        long quantityTraded = quantityTraded(buyOrder.getQuantity(), sellOrder.getQuantity());
        return new Trade(buyOrder.getPrice(), quantityTraded, buyOrder.getOrderId(), sellOrder.getOrderId());
    }

    private void updateOrderBookAfterTrade(final OrderBook orderBook, final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        orderBook.updateOrderBookAfterTrade(buyOrder, sellOrder, quantityTraded);
    }

    private long quantityTraded(final long buyQuantity, final long sellQuantity) {
        return Math.min(buyQuantity, sellQuantity);
    }

    private boolean isOneSided(final Order buyOrder, final Order sellOrder) {
        return buyOrder == null || sellOrder == null;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}
