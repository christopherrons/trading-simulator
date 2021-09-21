package com.christopher.herron.tradingsimulator.domain.model;

public class Metric {

    private final double tradesPerSecond;
    private final double ordersPerSecond;
    private final long tradesGenerated;
    private final long ordersGenerated;

    public Metric(double tradesPerSecond, double ordersPerSecond, long tradesGenerated, long ordersGenerated) {
        this.tradesPerSecond = tradesPerSecond;
        this.ordersPerSecond = ordersPerSecond;
        this.tradesGenerated = tradesGenerated;
        this.ordersGenerated = ordersGenerated;
    }

    public double getTradesPerSecond() {
        return tradesPerSecond;
    }

    public double getOrdersPerSecond() {
        return ordersPerSecond;
    }

    public long getTradesGenerated() {
        return tradesGenerated;
    }

    public long getOrdersGenerated() {
        return ordersGenerated;
    }
}
