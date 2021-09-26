package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.utils.MathUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Trade {

    private final Instant timeStamp = Instant.now();
    private final long price;
    private final long quantity;
    private final long buyOrderId;
    private final long sellOrderId;
    private final long tradeId;
    private final String instrumentId;
    private final short decimalsInPrice;

    public Trade(long price, long quantity, long buyOrderId, long sellOrderId, long tradeId, String instrumentId, short decimalsInPrice) {
        this.price = price;
        this.quantity = quantity;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.tradeId = tradeId;
        this.instrumentId = instrumentId;
        this.decimalsInPrice = decimalsInPrice;
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

    public short getDecimalsInPrice() {
        return decimalsInPrice;
    }

    public long getTradeId() {
        return tradeId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getTimeStampHourMiniteSecond() {
        final SimpleDateFormat formatterHourMinuteSecond = new SimpleDateFormat("HH:mm:ss");
        return formatterHourMinuteSecond.format(Date.from(this.timeStamp));
    }

    public double getPriceAsDouble() {
        return MathUtils.convertToDouble(this.price, this.decimalsInPrice);
    }
}
