package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

@Component
public class OrderBookCache {

    private final TreeMap<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();
    private long totalNumberOfOrders;

    public OrderBookCache() {
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

        totalNumberOfOrders++;
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

    public long generateOrderId() {
        return totalNumberOfOrders + 1;
    }

    public long getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }
}
