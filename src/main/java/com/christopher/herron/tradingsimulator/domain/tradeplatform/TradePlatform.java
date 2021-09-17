package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class TradePlatform {

    private final OrderBook orderBook;
    private final List<Trade> trades = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    public TradePlatform(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public List<Trade> getTrades() {
        readWriteLock.readLock().lock();
        try {
            return trades;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void addTrade(Trade trade) {
        trades.add(0, trade);
    }

    public long generateTradeId() {
        return trades.size() + 1;
    }

}
