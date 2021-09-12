package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.MatchingEngine;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradePlatform;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradeSimulator;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.domain.transactions.Trade;
import com.christopher.herron.tradingsimulator.domain.utils.TradePlatformUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderService {

    private final int MAX_USER_ORDERS_IN_TABLE = 10;
    private final int MAX_TRADES_IN_TABLE = 10;
    private final int MAX_ORDERBOOK_ORDERS_IN_TABLE = 5;
    private final String USER = "CHR";
    private final TradePlatform tradePlatform;
    private final MatchingEngine matchingEngine;

    @Autowired
    public OrderService(TradePlatform tradePlatform, MatchingEngine matchingEngine) {
        this.tradePlatform = tradePlatform;
        this.matchingEngine = matchingEngine;
    }

    public String orderPostRequest(@ModelAttribute Order order, Model model) {
        order.setOrderId(tradePlatform.getTotalNrOfOrdersEntered() + 1);
        order.setUserId(USER);
        tradePlatform.addUserOrder(order);

        addOrder(order);
        matchOrders();

        model.addAttribute("tradeSimulator", new TradeSimulator());
        model.addAttribute("trades", getNLatestTrades(MAX_TRADES_IN_TABLE));
        model.addAttribute("buyOrders", getNBestBuyOrders(MAX_ORDERBOOK_ORDERS_IN_TABLE));
        model.addAttribute("sellOrders", getNBestSellOrders(MAX_ORDERBOOK_ORDERS_IN_TABLE));
        Map<Instant, Long> trades = tradePlatform.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTradeTimeStamp, Trade::getPrice));
        model.addAttribute("tradesGraph", trades);
        model.addAttribute("openOrders", tradePlatform.getNUserOrders(MAX_USER_ORDERS_IN_TABLE, USER, OrderStatusEnum.OPEN));

        return "index";
    }

    public String runSimulationPostRequest(@ModelAttribute TradeSimulator tradeSimulator, Model model) throws InterruptedException {
        for (int i = 0; i < tradeSimulator.getOrdersToGenerate(); i++) {
            Order order = Order.valueOf(
                    tradePlatform.getTotalNrOfOrdersEntered() + 1,
                    String.format("Bot: %s", tradePlatform.getTotalNrOfUsers() + 1),
                    OrderStatusEnum.OPEN.getValue(),
                    Instant.now(),
                    TradePlatformUtils.generateQuantity(),
                    TradePlatformUtils.generatePrice(),
                    TradePlatformUtils.getRandomOrderType()
            );
            //  Thread.sleep(1);
            tradePlatform.addUserOrder(order);

            addOrder(order);
            matchOrders();
        }

        model.addAttribute("order", new Order());
        model.addAttribute("trades", getNLatestTrades(MAX_TRADES_IN_TABLE));
        model.addAttribute("buyOrders", getNBestBuyOrders(MAX_ORDERBOOK_ORDERS_IN_TABLE));
        model.addAttribute("sellOrders", getNBestSellOrders(MAX_ORDERBOOK_ORDERS_IN_TABLE));
        Map<Instant, Long> trades = tradePlatform.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTradeTimeStamp, Trade::getPrice));
        model.addAttribute("tradesGraph", trades);

        return "index";
    }

    public List<Order> getNBestBuyOrders(final int maxOrdersInTable) {
        List<Order> buyOrders = tradePlatform.getBuyOrders();
        return buyOrders.size() > maxOrdersInTable ? buyOrders.subList(buyOrders.size() - maxOrdersInTable, buyOrders.size()) : buyOrders;
    }

    public List<Order> getNBestSellOrders(final int maxOrdersInTable) {
        List<Order> sellOrders = tradePlatform.getSellOrders();
        return sellOrders.size() > maxOrdersInTable ? sellOrders.subList(sellOrders.size() - maxOrdersInTable, sellOrders.size()) : sellOrders;
    }

    public List<Trade> getNLatestTrades(final int maxTradesInTable) {
        List<Trade> trades = tradePlatform.getTrades();
        Collections.reverse(trades);
        return trades.size() > maxTradesInTable ? trades.subList(trades.size() - maxTradesInTable, trades.size()) : trades;
    }

    public void addOrder(final Order order) {
        tradePlatform.addOrder(order);
    }

    public void matchOrders() {
        matchingEngine.matchOrders();
    }

}
