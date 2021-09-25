package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TradeCache {

    private final Map<Long, Trade> tradeIdToTrade = new ConcurrentHashMap<>();

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

}
