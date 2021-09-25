package com.christopher.herron.tradingsimulator.domain.model;

public class TradeSimulation {

    private long ordersToGenerate;
    private int ordersPerSecond;

    public TradeSimulation() {
    }

    public long getOrdersToGenerate() {
        return ordersToGenerate;
    }

    public void setOrdersToGenerate(long ordersToGenerate) {
        this.ordersToGenerate = ordersToGenerate;
    }

    public int getOrdersPerSecond() {
        return ordersPerSecond;
    }

    public void setOrdersPerSecond(int ordersPerSecond) {
        this.ordersPerSecond = ordersPerSecond;
    }
}
