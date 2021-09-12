package com.christopher.herron.tradingsimulator.domain.tradeplatform;

public class TradeSimulator {

    private int ordersToGenerate;

    public TradeSimulator() {
    }

    public int getOrdersToGenerate() {
        return ordersToGenerate;
    }

    public void setOrdersToGenerate(int ordersToGenerate) {
        this.ordersToGenerate = ordersToGenerate;
    }
}
