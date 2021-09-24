package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookCache {

    private final Map<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final Map<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();
    private final Map<String, Map<OrderStatusEnum, Map<Long, Order>>> userIdToOrderStatusToOrderIdToOrder = new ConcurrentHashMap<>();
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

        userIdToOrderStatusToOrderIdToOrder.computeIfAbsent(order.getUserId(), key -> new ConcurrentHashMap<>())
                .computeIfAbsent(OrderStatusEnum.fromValue(order.getOrderStatus()), key -> new ConcurrentHashMap<>()).put(order.getOrderId(), order);

        totalNumberOfOrders++;
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        buyOrder.updateCurrentQuantity(quantityTraded);
        sellOrder.updateCurrentQuantity(quantityTraded);

        if (buyOrder.isOrderFilled()) {
            removeBestBuyOrder();
        }

        if (sellOrder.isOrderFilled()) {
            removeBestSellOrder();
        }
    }

    private void removeBestBuyOrder() {
        removeBestOrder(buyPriceToOrders);
    }

    private void removeBestSellOrder() {
        removeBestOrder(sellPriceToOrders);
    }

    private void removeBestOrder(Map<Long, PriorityQueue<Order>> priceToOrders) {
        PriorityQueue<Order> bestOrderPriorityQueue = priceToOrders.values().iterator().next();
        Order order = bestOrderPriorityQueue.poll();
        if (bestOrderPriorityQueue.isEmpty()) {
            priceToOrders.remove(order.getPrice());
        }
        order.setOrderStatus(OrderStatusEnum.FILLED.getValue());
        updateUserOrdersMapAfterTrade(order.getUserId(), order.getOrderId());
    }

    private void updateUserOrdersMapAfterTrade(String userId, long orderId) {
        updateUserOrdersMap(userId, orderId, OrderStatusEnum.OPEN, OrderStatusEnum.FILLED);
    }

    private void updateUserOrdersMap(String userId, long orderId, OrderStatusEnum fromOrderStatus, OrderStatusEnum toOrderStatus) {
        userIdToOrderStatusToOrderIdToOrder.get(userId)
                .computeIfAbsent(toOrderStatus, key -> new ConcurrentHashMap<>())
                .put(orderId, userIdToOrderStatusToOrderIdToOrder.get(userId).get(fromOrderStatus).remove(orderId));
    }

    public List<Order> getBuyOrders() {
        List<Order> buyOrders = new ArrayList<>();
        for (PriorityQueue<Order> buyQueue : buyPriceToOrders.values()) {
            buyOrders.addAll(buyQueue);
        }
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

    public List<Order> getUserOrders(final String userId, final OrderStatusEnum orderStatus) {
        if (userIdToOrderStatusToOrderIdToOrder.containsKey(userId)) {
            Collection<Order> orders = userIdToOrderStatusToOrderIdToOrder.get(userId)
                    .get(orderStatus).values();
            return new ArrayList<>(orders);
        } else {
            return Collections.emptyList();
        }
    }

    public long generateOrderId() {
        return totalNumberOfOrders + 1;
    }

    public long getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }
}
