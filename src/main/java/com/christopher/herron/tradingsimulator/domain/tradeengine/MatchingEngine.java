package com.christopher.herron.tradingsimulator.domain.tradeengine;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.service.OrderBookService;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.service.UserService;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchingEngine {

    private final TradeService tradeService;
    private final OrderBookService orderBookService;
    private final UserService userService;

    @Autowired
    public MatchingEngine(TradeService tradeService, OrderBookService orderBookService, UserService userService) {
        this.tradeService = tradeService;
        this.orderBookService = orderBookService;
        this.userService = userService;
    }

    public void matchOrders(final String instrumentId) {
        while (true) {
            Order buyOrder = orderBookService.getBestBuyOrder(instrumentId);
            Order sellOrder = orderBookService.getBestSellOrder(instrumentId);

            if (isOneSided(buyOrder, sellOrder)) {
                return;
            }

            if (isMatch(buyOrder.getPrice(), sellOrder.getPrice())) {
                runPostTradeProcess(buyOrder, sellOrder);
            } else {
                break;
            }
        }
    }

    private boolean isMatch(final long buyPrice, final long sellPrice) {
        return buyPrice >= sellPrice;
    }

    private void runPostTradeProcess(final Order buyOrder, final Order sellOrder) {
        Trade trade = createTrade(buyOrder, sellOrder);
        tradeService.addTrade(trade);

        orderBookService.updateOrderBookAfterTrade(buyOrder, sellOrder, trade.getQuantity());

        if (buyOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            userService.updateUserOrderTableView(buyOrder, trade);
        }
        if (sellOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            userService.updateUserOrderTableView(sellOrder, trade);
        }
    }

    private Trade createTrade(final Order buyOrder, final Order sellOrder) {
        long quantityTraded = quantityTraded(buyOrder.getCurrentQuantity(), sellOrder.getCurrentQuantity());
        if (isBuyOrderTaker(buyOrder, sellOrder)) {
            return new Trade(
                    buyOrder.getPrice(), quantityTraded, buyOrder.getOrderId(),
                    sellOrder.getOrderId(), tradeService.generateTradeId(), buyOrder.getInstrumentId(), buyOrder.getDecimalsInPrice()
            );
        } else {
            return new Trade(
                    sellOrder.getPrice(), quantityTraded, buyOrder.getOrderId(),
                    sellOrder.getOrderId(), tradeService.generateTradeId(), sellOrder.getInstrumentId(), sellOrder.getDecimalsInPrice()
            );
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
