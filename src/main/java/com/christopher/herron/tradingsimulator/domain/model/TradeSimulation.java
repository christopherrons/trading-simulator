package com.christopher.herron.tradingsimulator.domain.model;

public class TradeSimulation {

    private int ordersToGenerate;
    private int ordersPerSecond;

    public TradeSimulation() {
    }

    public int getOrdersToGenerate() {
        return ordersToGenerate;
    }

    public void setOrdersToGenerate(int ordersToGenerate) {
        this.ordersToGenerate = ordersToGenerate;
    }

    public int getOrdersPerSecond() {
        return ordersPerSecond;
    }

    public void setOrdersPerSecond(int ordersPerSecond) {
        this.ordersPerSecond = ordersPerSecond;
    }
}
