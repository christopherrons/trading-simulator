package com.christopher.herron.tradingsimulator.domain.model;

public class Limit {

    private final long price;
    public OrderBookEntry head;
    public OrderBookEntry tail;
    private long totalLimitQuantity;

    public Limit(long price) {
        this.price = price;
    }

    public void addOrderBookEntry(final OrderBookEntry orderBookEntry) {
        if (head == null) {
            head = orderBookEntry;
        } else {
            orderBookEntry.previous = tail;
            tail.next = orderBookEntry;
        }
        tail = orderBookEntry;
        totalLimitQuantity = totalLimitQuantity + orderBookEntry.getOrder().getInitialQuantity();
    }

    public long getPrice() {
        return price;
    }

    public long getTotalLimitQuantity() {
        return totalLimitQuantity;
    }

    public void decreaseLimitQuantity(final long quantityTraded) {
        this.totalLimitQuantity = this.totalLimitQuantity - quantityTraded;
    }
}
