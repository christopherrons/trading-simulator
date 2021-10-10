package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.TradeCache;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.event.UpdateTradGraphViewEvent;
import com.christopher.herron.tradingsimulator.view.event.UpdateTradeTableViewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TradeService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final TradeCache tradeCache;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public TradeService(ApplicationEventPublisher applicationEventPublisher, TradeCache tradeCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.tradeCache = tradeCache;
    }

    public void addTrades(final List<Trade> trades) {
        for (Trade trade : trades) {
            addTrade(trade);
        }
    }

    public void addTrade(final Trade trade) {
        tradeCache.addTrade(trade);
        updateTradeViews(trade);
    }

    private void updateTradeViews(final Trade trade) {
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateTradGraphViewEvent(this, trade));
                applicationEventPublisher.publishEvent(new UpdateTradeTableViewEvent(this, trade));
            }
        });
    }

    public long generateTradeId() {
        return tradeCache.generateTradeId();
    }

    public long getTotalNumberOfTrades() {
        return tradeCache.getTotalNumberOfTrades();
    }

    public double getLatestPrice() {
        return tradeCache.getLatestPrice();
    }
}
