package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.data.cache.ClientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderBook {

    private final Map<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final Map<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();
    private final ClientCache clientCache;
    private long orderNumber;

    @Autowired
    public OrderBook(ClientCache clientCache) {
        this.clientCache = clientCache;
    }

    public void addOrder(final Order order) {
        orderNumber++;
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
        Order order = priorityQueueBestBuyOrder.poll();
        if (priorityQueueBestBuyOrder.isEmpty()) {
            buyPriceToOrders.remove(order.getPrice());
        }
        clientCache.updateClientOrderStatus(order.getClientId(), order.getOrderId(), OrderStatusEnum.fromValue(order.getOrderStatus()), OrderStatusEnum.FILLED);
    }

    private void removeBestSellOrder() {
        PriorityQueue<Order> priorityQueueBestSellOrder = sellPriceToOrders.values().iterator().next();
        Order order = priorityQueueBestSellOrder.poll();
        if (priorityQueueBestSellOrder.isEmpty()) {
            sellPriceToOrders.remove(order.getPrice());
        }
        clientCache.updateClientOrderStatus(order.getClientId(), order.getOrderId(), OrderStatusEnum.fromValue(order.getOrderStatus()), OrderStatusEnum.FILLED);
    }

    public long getOrderNumber() {
        return orderNumber;
    }
}
