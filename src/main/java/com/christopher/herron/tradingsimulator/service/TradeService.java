package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.TradeCache;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.TradeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeService {

    private final TradeCache tradeCache;
    private final TradeView tradeView;

    @Autowired
    public TradeService(TradeCache tradeCache, TradeView tradeView) {
        this.tradeCache = tradeCache;
        this.tradeView = tradeView;
    }

    public List<Trade> getTrades() {
        return tradeCache.getTrades();
    }

    public void addTrade(final Trade trade) {
        tradeCache.addTrade(trade);
        updateTradeTableView(trade);
    }

    public long generateTradeId() {
        return tradeCache.generateTradeId();
    }

    public void updateTradeTableView(final Trade trade) {
        tradeView.updateTradeTableView(trade);
    }

    public long getTotalNumberOfTrades() {
        return tradeCache.getTotalNumberOfTrades();
    }
}
