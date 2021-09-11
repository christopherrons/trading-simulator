package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderBook {

    private final Map<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final Map<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();

    public OrderBook() {
    }

    public void addOrder(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOrders.computeIfAbsent(order.getPrice(), value -> new PriorityQueue<>()).add(order);
                break;
            case SELL:
                sellPriceToOrders.computeIfAbsent(order.getPrice(), value -> new PriorityQueue<>()).add(order);
                break;
            default:
                break;
        }
    }

    public List<Order> getBuyOrders() {
        List<Order> buyOrders = new ArrayList<>();
        for (PriorityQueue<Order> buyQueue : buyPriceToOrders.values()) {
            buyOrders.addAll(buyQueue);
        }
        Collections.reverse(buyOrders);
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        List<Order> sellOrders = new ArrayList<>();
        for (PriorityQueue<Order> buyQueue : sellPriceToOrders.values()) {
            sellOrders.addAll(buyQueue);
        }
        return sellOrders;
    }

    public Order getBestBuyOrder() {
        Iterator<PriorityQueue<Order>> priorityQueueIterator = buyPriceToOrders.values().iterator();
        if (priorityQueueIterator.hasNext()) {
            return buyPriceToOrders.values().iterator().next().peek();
        } else {
            return null;
        }
    }

    public Order getBestSellOrder() {
        Iterator<PriorityQueue<Order>> priorityQueueIterator = sellPriceToOrders.values().iterator();
        if (priorityQueueIterator.hasNext()) {
            return sellPriceToOrders.values().iterator().next().peek();
        } else {
            return null;
        }
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        buyOrder.updateQuantity(quantityTraded);
        sellOrder.updateQuantity(quantityTraded);

        if (buyOrder.isOrderFilled()) {
            removeBestBuyOrder();
        }

        if (sellOrder.isOrderFilled()) {
            removeBestSellOrder();
        }
    }

    private void removeBestBuyOrder() {
        PriorityQueue<Order> priorityQueueBestBuyOrder = buyPriceToOrders.values().iterator().next();
        Order removedOrder = priorityQueueBestBuyOrder.poll();
        if (priorityQueueBestBuyOrder.isEmpty()) {
            buyPriceToOrders.remove(removedOrder.getPrice());
        }
    }

    private void removeBestSellOrder() {
        PriorityQueue<Order> priorityQueueBestSellOrder = sellPriceToOrders.values().iterator().next();
        Order removedOrder = priorityQueueBestSellOrder.poll();
        if (priorityQueueBestSellOrder.isEmpty()) {
            sellPriceToOrders.remove(removedOrder.getPrice());
        }
    }
}
