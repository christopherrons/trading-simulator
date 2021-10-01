package com.christopher.herron.tradingsimulator.domain.model;

public class OrderBookEntry {

    public final Limit limit;
    private final Order order;
    public OrderBookEntry previous;
    public OrderBookEntry next;

    public OrderBookEntry(Order order, Limit limit) {
        this.order = order;
        this.limit = limit;
    }

    public Order getOrder() {
        return order;
    }

    public Limit getLimit() {
        return limit;
    }

    public boolean isOnlyTail() {
        return previous != null && next == null;
    }

    public boolean isOnlyHead() {
        return previous == null && next != null;
    }

    public boolean isInMiddle() {
        return previous != null && next != null;
    }

    public boolean isHeadAndTails() {
        return limit.head == this && limit.tail == this;
    }
}
