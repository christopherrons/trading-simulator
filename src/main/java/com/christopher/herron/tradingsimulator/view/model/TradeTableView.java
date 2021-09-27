package com.christopher.herron.tradingsimulator.view.model;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;

import java.util.ArrayList;
import java.util.List;

public class TradeTableView {

    private final static int maxTradesInTable = ViewConfigs.getMaxTradesInTable();
    private final List<Trade> trades = new ArrayList<>();

    public TradeTableView() {
    }

    public void updateTradeTableData(final Trade trade) {
        if (trades.size() >= maxTradesInTable) {
            trades.remove(maxTradesInTable - 1);
        }
        trades.add(0, trade);
    }

    public List<Trade> getTrades() {
        return trades;
    }
}
