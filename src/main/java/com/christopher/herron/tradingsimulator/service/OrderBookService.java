package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderAction;
import com.christopher.herron.tradingsimulator.domain.cache.OrderBookCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.event.UpdateOrderBookViewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OrderBookService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderBookCache orderBookCache;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public OrderBookService(ApplicationEventPublisher applicationEventPublisher, OrderBookCache orderBookCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderBookCache = orderBookCache;
    }

    public void writeToOrderBook(final Order order) {
        switch (OrderAction.fromValue(order.getOrderAction())) {
            case ADD:
                orderBookCache.addOrderToOrderBook(order);
                updateOrderBookViewNewOrder(order.copy());
                break;
            case DELETE:
            case UPDATE:
            default:
        }
    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, long tradeQuantity) {
        orderBookCache.updateOrderBookAfterTrade(buyOrder, sellOrder, tradeQuantity);
        updateOrderBookViewAfterTrade(buyOrder.copy(), sellOrder.copy());
    }

    private void updateOrderBookViewNewOrder(final Order order) {
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateOrderBookViewEvent(this, order));
            }
        });
    }

    private void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellorder) {
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateOrderBookViewEvent(this, buyOrder, sellorder));
            }
        });
    }

    public Order getBestBuyOrder(final String instrumentId) {
        return orderBookCache.getBestBuyOrder(instrumentId);
    }

    public Order getBestSellOrder(final String instrumentId) {
        return orderBookCache.getBestSellOrder(instrumentId);
    }

    public long generateOrderId() {
        return orderBookCache.generateOrderId();
    }

    public long getTotalNumberOfOrders() {
        return orderBookCache.getTotalNumberOfOrders();
    }

}
