package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradePlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeService {

    private final TradePlatform tradePlatform;

    @Autowired
    public TradeService(TradePlatform tradePlatform) {
        this.tradePlatform = tradePlatform;
    }

    public List<Trade> getTrades() {
        return tradePlatform.getTrades();
    }

    public void addTrade(Trade trade) {
        tradePlatform.addTrade(trade);
    }

    public long generateTradeId() {
        return tradePlatform.generateTradeId();
    }

}
