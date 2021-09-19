package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.domain.tradeengine.TradePlatform;
import com.christopher.herron.tradingsimulator.view.TradeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeService {

    private final TradePlatform tradePlatform;
    private final TradeView tradeView;

    @Autowired
    public TradeService(TradePlatform tradePlatform, TradeView tradeView) {
        this.tradePlatform = tradePlatform;
        this.tradeView = tradeView;
    }

    public List<Trade> getTrades() {
        return tradePlatform.getTrades();
    }

    public void addTrade(final Trade trade) {
        tradePlatform.addTrade(trade);
        updateTradeTableView(trade);
    }

    public long generateTradeId() {
        return tradePlatform.generateTradeId();
    }

    public void updateTradeTableView(final Trade trade) {
        tradeView.updateTradeTableView(trade);
    }

}
