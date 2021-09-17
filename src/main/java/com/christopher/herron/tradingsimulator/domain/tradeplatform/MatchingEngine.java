package com.christopher.herron.tradingsimulator.domain.tradeplatform;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.service.OrderBookService;
import com.christopher.herron.tradingsimulator.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchingEngine {

    private final TradeService tradeService;
    private final OrderBookService orderBookService;

    @Autowired
    public MatchingEngine(TradeService tradeService, OrderBookService orderBookService) {
        this.tradeService = tradeService;
        this.orderBookService = orderBookService;
    }

    public void matchOrders() {
        while (true) {
            Order buyOrder = orderBookService.getBestBuyOrder();
            Order sellOrder = orderBookService.getBestSellOrder();

            if (isOneSided(buyOrder, sellOrder)) {
                return;
            }

            if (isMatch(buyOrder.getPrice(), sellOrder.getPrice())) {
                runSettlementProcess(buyOrder, sellOrder);

            } else {
                break;
            }
        }
    }

    private boolean isMatch(final long buyPrice, final long sellPrice) {
        return buyPrice >= sellPrice;
    }

    private void runSettlementProcess(final Order buyOrder, final Order sellOrder) {
        Trade trade = createTrade(buyOrder, sellOrder);
        tradeService.addTrade(trade);
        orderBookService.updateOrderBookAfterTrade(buyOrder, sellOrder, trade.getQuantity());
    }

    private Trade createTrade(final Order buyOrder, final Order sellOrder) {
        long quantityTraded = quantityTraded(buyOrder.getCurrentQuantity(), sellOrder.getCurrentQuantity());
        if (isBuyOrderTaker(buyOrder, sellOrder)) {
            return new Trade(buyOrder.getPrice(), quantityTraded, buyOrder.getOrderId(), sellOrder.getOrderId(), tradeService.generateTradeId());
        } else {
            return new Trade(sellOrder.getPrice(), quantityTraded, buyOrder.getOrderId(), sellOrder.getOrderId(), tradeService.generateTradeId());
        }
    }

    private long quantityTraded(final long buyQuantity, final long sellQuantity) {
        return Math.min(buyQuantity, sellQuantity);
    }

    private boolean isOneSided(final Order buyOrder, final Order sellOrder) {
        return buyOrder == null || sellOrder == null;
    }

    private boolean isBuyOrderTaker(final Order buyOrder, final Order sellOrder) {
        return buyOrder.getTimeStamp().toEpochMilli() > sellOrder.getTimeStamp().toEpochMilli();
    }


}
