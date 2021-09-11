package com.christopher.herron.tradingsimulator.domain;

import java.util.Date;

public class Trade {

    private final Date tradeTimeStamp = new Date();
    private final long price;
    private final long quantity;
    private final long buyOrderId;
    private final long sellOrderId;

    public Trade(long price, long quantity, long buyOrderId, long sellOrderId) {
        this.price = price;
        this.quantity = quantity;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
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

    public Date getTradeTimeStamp() {
        return tradeTimeStamp;
    }
}
