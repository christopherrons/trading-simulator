package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.TradeCache;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.TradeGraphView;
import com.christopher.herron.tradingsimulator.view.TradeTableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeService {

    private final TradeCache tradeCache;
    private final TradeTableView tradeTableView;
    private final TradeGraphView tradeGraphView;

    @Autowired
    public TradeService(TradeCache tradeCache, TradeTableView tradeTableView, TradeGraphView tradeGraphView) {
        this.tradeCache = tradeCache;
        this.tradeTableView = tradeTableView;
        this.tradeGraphView = tradeGraphView;
    }

    public List<Trade> getTrades() {
        return tradeCache.getTrades();
    }

    public void addTrade(final Trade trade) {
        tradeCache.addTrade(trade);
        updateTradeViews(trade);
    }

    private void updateTradeViews(final Trade trade) {
        updateTradeTableView(trade);
        updateTradeGraphView(trade);
    }

    public long generateTradeId() {
        return tradeCache.generateTradeId();
    }

    public void updateTradeTableView(final Trade trade) {
        tradeTableView.updateTradeTableView(trade);
    }

    public void updateTradeGraphView(final Trade trade) {
        tradeGraphView.updateTradeGraphView(trade);
    }

    public long getTotalNumberOfTrades() {
        return tradeCache.getTotalNumberOfTrades();
    }
}
