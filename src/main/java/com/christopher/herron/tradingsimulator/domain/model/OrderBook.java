package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;

import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class OrderBook {

    private final TreeMap<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();

    public OrderBook() {
    }

    public void addOrderToOrderBook(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOrders.computeIfAbsent(order.getPrice(), key -> new PriorityQueue<>()).add(order);
                break;
            case SELL:
                sellPriceToOrders.computeIfAbsent(order.getPrice(), key -> new PriorityQueue<>()).add(order);
                break;
            default:
                break;
        }
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        buyOrder.updateCurrentQuantity(quantityTraded);
        sellOrder.updateCurrentQuantity(quantityTraded);

        if (buyOrder.isOrderFilled()) {
            removeBestBuyOrder();
            buyOrder.setOrderStatus(OrderStatusEnum.FILLED.getValue());
        }

        if (sellOrder.isOrderFilled()) {
            removeBestSellOrder();
            sellOrder.setOrderStatus(OrderStatusEnum.FILLED.getValue());
        }
    }

    private void removeBestBuyOrder() {
        removeBestOrder(buyPriceToOrders);
    }

    private void removeBestSellOrder() {
        removeBestOrder(sellPriceToOrders);
    }

    private void removeBestOrder(TreeMap<Long, PriorityQueue<Order>> priceToOrders) {
        Map.Entry<Long, PriorityQueue<Order>> orderEntry = priceToOrders.firstEntry();
        Order order = orderEntry.getValue().poll();
        if (orderEntry.getValue().isEmpty()) {
            priceToOrders.remove(order.getPrice());
        }
    }

    public Order getBestBuyOrder() {
        return buyPriceToOrders.isEmpty() ? null : buyPriceToOrders.firstEntry().getValue().peek();
    }

    public Order getBestSellOrder() {
        return sellPriceToOrders.isEmpty() ? null : sellPriceToOrders.firstEntry().getValue().peek();
    }
}
