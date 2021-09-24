package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradeCache {

    private final List<Trade> trades = new ArrayList<>();

    public TradeCache() {
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void addTrade(Trade trade) {
        trades.add(trade);
    }

    public long generateTradeId() {
        return trades.size() + 1;
    }
    public long getTotalNumberOfTrades() {
        return trades.size();
    }

}
