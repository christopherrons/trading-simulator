package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@Component
public class TradeCache {

    private final TreeMap<Long, Trade> tradeIdToTrade = new TreeMap<>();

    public TradeCache() {
    }

    public void addTrade(Trade trade) {
        tradeIdToTrade.putIfAbsent(trade.getTradeId(), trade);
    }

    public long generateTradeId() {
        return tradeIdToTrade.size() + 1;
    }

    public long getTotalNumberOfTrades() {
        return tradeIdToTrade.size();
    }

    public double getLatestPrice() {
        return tradeIdToTrade.isEmpty() ? 100.0 : tradeIdToTrade.lastEntry().getValue().getPriceAsDouble();
    }

}
