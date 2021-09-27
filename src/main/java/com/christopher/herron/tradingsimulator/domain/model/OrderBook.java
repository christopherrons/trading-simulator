package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class OrderBook {

    private final TreeMap<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>();
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
        if (buyOrder.isOrderFilled()) {
            removeBestBuyOrder();
            buyOrder.setOrderStatus(OrderStatusEnum.FILLED.getValue());
        }

        sellOrder.updateCurrentQuantity(quantityTraded);
        if (sellOrder.isOrderFilled()) {
            removeBestSellOrder();
            sellOrder.setOrderStatus(OrderStatusEnum.FILLED.getValue());
        }
    }

    private void removeBestBuyOrder() {
        Map.Entry<Long, PriorityQueue<Order>> orderEntry = buyPriceToOrders.lastEntry();
        Order order = orderEntry.getValue().poll();
        if (orderEntry.getValue().isEmpty()) {
            buyPriceToOrders.remove(order.getPrice());
        }
    }

    private void removeBestSellOrder() {
        Map.Entry<Long, PriorityQueue<Order>> orderEntry = sellPriceToOrders.firstEntry();
        Order order = orderEntry.getValue().poll();
        if (orderEntry.getValue().isEmpty()) {
            sellPriceToOrders.remove(order.getPrice());
        }
    }

    public Order getBestBuyOrder() {
        return buyPriceToOrders.isEmpty() ? null : buyPriceToOrders.lastEntry().getValue().peek();
    }

    public Order getBestSellOrder() {
        return sellPriceToOrders.isEmpty() ? null : sellPriceToOrders.firstEntry().getValue().peek();
    }
}
