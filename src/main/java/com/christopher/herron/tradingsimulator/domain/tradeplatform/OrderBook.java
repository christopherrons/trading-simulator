package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.service.UserService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderBook {

    private final Map<Long, PriorityQueue<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    private final Map<Long, PriorityQueue<Order>> sellPriceToOrders = new TreeMap<>();
    private final UserService userService;
    private long totalNumberOfOrders;

    public OrderBook(UserService userService) {
        this.userService = userService;
    }

    public void addOrderToOrderBook(final Order order) {
        totalNumberOfOrders++;
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
        userService.updateUserOrderStatus(order.getUserId(), order.getOrderId(), OrderStatusEnum.fromValue(order.getOrderStatus()), OrderStatusEnum.FILLED);
    }

    public long generateOrderId() {
        return totalNumberOfOrders + 1;
    }
}
