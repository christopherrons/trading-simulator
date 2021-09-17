package com.christopher.herron.tradingsimulator.config;

import com.christopher.herron.tradingsimulator.common.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.service.OrderBookService;
import com.christopher.herron.tradingsimulator.service.OrderService;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableScheduling
@Configuration
public class Scheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final OrderService orderService;
    private final TradeService tradeService;
    private final OrderBookService orderBookService;
    private final int MAX_USER_ORDERS_IN_TABLE = 10;
    private final int MAX_TRADES_IN_TABLE = 10;
    private final int MAX_ORDERBOOK_ORDERS_IN_TABLE = 5;
    private final String USER = "CHR";

    @Autowired
    public Scheduler(SimpMessagingTemplate messagingTemplate, UserService userService, OrderService orderService, TradeService tradeService, OrderBookService orderBookService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.orderService = orderService;
        this.tradeService = tradeService;
        this.orderBookService = orderBookService;
    }

    @Scheduled(fixedRate = 20000)
    public void updateOpenOrderView() {
        List<Order> openOrders = userService.getUserOpenOrders(USER);
        Collections.reverse(openOrders);
        openOrders = openOrders.size() > MAX_USER_ORDERS_IN_TABLE ? openOrders.subList(openOrders.size() - MAX_USER_ORDERS_IN_TABLE, openOrders.size()) : openOrders;

        messagingTemplate.convertAndSend("/topic/openOrders", new DataTableWrapper<>(openOrders));
    }

    @Scheduled(fixedRate = 10000)
    public void updateOrderBookView() {
        List<Order> buyOrders = orderBookService.getBuyOrders();
        List<Order> bestBuyOrdes = buyOrders.size() > MAX_ORDERBOOK_ORDERS_IN_TABLE ? buyOrders.subList(0, MAX_ORDERBOOK_ORDERS_IN_TABLE) : buyOrders;

        Collections.reverse(bestBuyOrdes);

        List<Order> sellOrders = orderBookService.getSellOrders();
        List<Order> bestSellOrders = sellOrders.size() > MAX_ORDERBOOK_ORDERS_IN_TABLE ? sellOrders.subList(0, MAX_ORDERBOOK_ORDERS_IN_TABLE) : sellOrders;

        List<Order> orderBookView = new ArrayList<>();
        orderBookView.addAll(bestBuyOrdes);
        orderBookView.addAll(bestSellOrders);

        messagingTemplate.convertAndSend("/topic/orderBook", new DataTableWrapper<>(orderBookView));
    }

    @Scheduled(fixedRate = 15000)
    public void updateTradeView() {
        List<Trade> trades = tradeService.getTrades();
        List<Trade> latestTrades = trades.size() > MAX_TRADES_IN_TABLE ? trades.subList(0, MAX_TRADES_IN_TABLE) : trades;

        messagingTemplate.convertAndSend("/topic/trades", new DataTableWrapper<>(latestTrades));
    }
}
