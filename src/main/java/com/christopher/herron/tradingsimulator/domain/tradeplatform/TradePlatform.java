package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradePlatform {


    private final List<Trade> trades = new ArrayList<>();

    public TradePlatform() {
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

}
