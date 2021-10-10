package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderActionEnum;
import com.christopher.herron.tradingsimulator.domain.cache.OrderBookCache;
import com.christopher.herron.tradingsimulator.domain.matchingengine.MatchingAlgorithmResults;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.ReadOnlyOrderBook;
import com.christopher.herron.tradingsimulator.view.event.UpdateOrderBookViewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class OrderBookService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderBookCache orderBookCache;
    private final MatchingEngineService matchingEngineService;
    private final InstrumentService instrumentService;
    private final TradeService tradeService;
    private final UserService userService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    public OrderBookService(ApplicationEventPublisher applicationEventPublisher, OrderBookCache orderBookCache, MatchingEngineService matchingEngineService, InstrumentService instrumentService, TradeService tradeService, UserService userService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderBookCache = orderBookCache;
        this.matchingEngineService = matchingEngineService;
        this.instrumentService = instrumentService;
        this.tradeService = tradeService;
        this.userService = userService;
    }

    public void updateOrderBook(final Order order) {
        readWriteLock.writeLock().lock();
        try {
            switch (OrderActionEnum.fromValue(order.getOrderAction())) {
                case ADD:
                    orderBookCache.addOrderToOrderBook(order, instrumentService.getInstrument(order.getInstrumentId()));
                    updateOrderBookView(order.copy());
                    matchOrders(orderBookCache.getOrderBook(order.getInstrumentId()));
                    break;
                case DELETE:
                    orderBookCache.removeOrderFromOrderBook(order);
                    break;
                case MODIFY:
                default:
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void matchOrders(final ReadOnlyOrderBook orderBook) {
        MatchingAlgorithmResults matchingAlgorithmResults = matchingEngineService.runMatchingEngine(orderBook);

        tradeService.addTrades(matchingAlgorithmResults.getTrades());

        userService.updateUserOrderTableView(matchingAlgorithmResults.getMatchedUserOrderTradePairs());

        updateOrderBook(matchingAlgorithmResults.getMatchedOrders());
    }

    private void updateOrderBook(final Set<Order> matchedOrders) {
        for (Order matchedOrder : matchedOrders) {
            if (matchedOrder.isOrderFilled()) {
                matchedOrder.setOrderAction(OrderActionEnum.DELETE.getValue());
                updateOrderBook(matchedOrder);
            }
            updateOrderBookView(matchedOrder.copy());
        }
    }

    private void updateOrderBookView(final Order order) {
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateOrderBookViewEvent(this, order));
            }
        });
    }

    public long generateOrderId() {
        return orderBookCache.generateOrderId();
    }

    public long getTotalNumberOfOrders() {
        return orderBookCache.getTotalNumberOfOrders();
    }

}
