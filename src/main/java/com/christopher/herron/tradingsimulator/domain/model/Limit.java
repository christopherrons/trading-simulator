package com.christopher.herron.tradingsimulator.domain.model;

public class Limit {

    private final long price;
    public OrderBookEntry head;
    public OrderBookEntry tail;

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
    }

    public long getPrice() {
        return price;
    }
}
