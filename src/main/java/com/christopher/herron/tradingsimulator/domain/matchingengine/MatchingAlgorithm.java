package com.christopher.herron.tradingsimulator.domain.matchingengine;

import com.christopher.herron.tradingsimulator.domain.model.Limit;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.ReadOnlyOrderBook;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.service.TradeService;

import java.util.Iterator;

public abstract class MatchingAlgorithm {

    public abstract MatchingAlgorithmResults matchOrders(final ReadOnlyOrderBook orderBook);

    protected boolean isMatch(final long buyPrice, final long sellPrice) {
        return buyPrice >= sellPrice;
    }

    protected long quantityTraded(final long buyQuantity, final long sellQuantity) {
        return Math.min(buyQuantity, sellQuantity);
    }

    protected boolean isOneSided(final Iterator<Limit> buyLimitIterator, final Iterator<Limit> sellLimitIterator) {
        return !buyLimitIterator.hasNext() || !sellLimitIterator.hasNext();
    }

    protected Trade createTrade(final long quantityTraded, final Order buyOrder, final Order sellOrder, TradeService tradeService) {
        return new Trade(isBuyOrderTaker(buyOrder, sellOrder) ? buyOrder.getPrice() : sellOrder.getPrice(), quantityTraded, buyOrder.getOrderId(),
                sellOrder.getOrderId(), tradeService.generateTradeId(), buyOrder.getInstrumentId(), buyOrder.getDecimalsInPrice()
        );
    }

    protected boolean isBuyOrderTaker(final Order buyOrder, final Order sellOrder) {
        return buyOrder.getTimeStamp().toEpochMilli() > sellOrder.getTimeStamp().toEpochMilli();
    }
}
