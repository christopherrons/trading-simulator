package com.christopher.herron.tradingsimulator.domain.model;

import java.time.Instant;

public class Trade {

    private final Instant timeStamp = Instant.now();
    private final long price;
    private final long quantity;
    private final long buyOrderId;
    private final long sellOrderId;
    private final long tradeId;

    public Trade(long price, long quantity, long buyOrderId, long sellOrderId, long tradeId) {
        this.price = price;
        this.quantity = quantity;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.tradeId = tradeId;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getBuyOrderId() {
        return buyOrderId;
    }

    public long getSellOrderId() {
        return sellOrderId;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public long getTradeId() {
        return tradeId;
    }
}