package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.domain.transactions.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchingEngine {

    private final TradePlatform tradePlatform;

    @Autowired
    public MatchingEngine(TradePlatform tradePlatform) {
        this.tradePlatform = tradePlatform;
    }

    public void matchOrders() {
        OrderBook orderBook = tradePlatform.getOrderBook();
        while (true) {
            Order buyOrder = orderBook.getBestBuyOrder();
            Order sellOrder = orderBook.getBestSellOrder();

            if (isOneSided(buyOrder, sellOrder)) {
                return;
            }

            if (isMatch(buyOrder.getPrice(), sellOrder.getPrice())) {
                runSettlementProcess(orderBook, buyOrder, sellOrder);

            } else {
                break;
            }
        }
    }

    private boolean isMatch(final long buyPrice, final long sellPrice) {
        return buyPrice >= sellPrice;
    }

    private void runSettlementProcess(final OrderBook orderBook, final Order buyOrder, final Order sellOrder) {
        Trade trade = createTrade(buyOrder, sellOrder);
        tradePlatform.addTrade(trade);
        updateOrderBookAfterTrade(orderBook, buyOrder, sellOrder, trade.getQuantity());
    }

    private Trade createTrade(final Order buyOrder, final Order sellOrder) {
        long quantityTraded = quantityTraded(buyOrder.getCurrentQuantity(), sellOrder.getCurrentQuantity());
        if (isBuyTaker(buyOrder, sellOrder)) {
            return new Trade(buyOrder.getPrice(), quantityTraded, buyOrder.getOrderId(), sellOrder.getOrderId(), tradePlatform.getTrades().size() + 1);
        } else {
            return new Trade(sellOrder.getPrice(), quantityTraded, buyOrder.getOrderId(), sellOrder.getOrderId(), tradePlatform.getTrades().size() + 1);
        }
    }

    private void updateOrderBookAfterTrade(final OrderBook orderBook, final Order buyOrder, final Order sellOrder, final long quantityTraded) {
        orderBook.updateOrderBookAfterTrade(buyOrder, sellOrder, quantityTraded);
    }

    private long quantityTraded(final long buyQuantity, final long sellQuantity) {
        return Math.min(buyQuantity, sellQuantity);
    }

    private boolean isOneSided(final Order buyOrder, final Order sellOrder) {
        return buyOrder == null || sellOrder == null;
    }

    private boolean isBuyTaker(final Order buyOrder, final Order sellOrder) {
        return buyOrder.getTimeStamp().toEpochMilli() > sellOrder.getTimeStamp().toEpochMilli();
    }


}
