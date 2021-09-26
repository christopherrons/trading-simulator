package com.christopher.herron.tradingsimulator.service;

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


    public void addOrderToOrderBook(final Order order) {
        orderBookCache.addOrderToOrderBook(order);
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateOrderBookViewEvent(this, order));
            }
        });

    }

    public void updateOrderBookAfterTrade(final Order buyOrder, final Order sellOrder, long tradeQuantity) {
        orderBookCache.updateOrderBookAfterTrade(buyOrder, sellOrder, tradeQuantity);

        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateOrderBookViewEvent(this, buyOrder, sellOrder));
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
