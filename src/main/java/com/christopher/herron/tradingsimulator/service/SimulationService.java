package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.MatchingEngine;
import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradePlatform;
import com.christopher.herron.tradingsimulator.domain.utils.TradePlatformUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SimulationService {

    private final TradePlatform tradePlatform;
    private final MatchingEngine matchingEngine;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public SimulationService(TradePlatform tradePlatform, MatchingEngine matchingEngine, UserService userService, OrderService orderService) {
        this.tradePlatform = tradePlatform;
        this.matchingEngine = matchingEngine;
        this.userService = userService;
        this.orderService = orderService;
    }

    public void runSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        for (int i = 0; i < tradeSimulation.getOrdersToGenerate(); i++) {
            Order order = Order.valueOf(
                    orderService.generateOrderId() + 1,
                    String.format("Bot: %s", userService.generateUserId()),
                    OrderStatusEnum.OPEN.getValue(),
                    Instant.now(),
                    TradePlatformUtils.generateQuantity(),
                    TradePlatformUtils.generatePrice(),
                    TradePlatformUtils.getRandomOrderType()
            );

            Thread.sleep(1000 / tradeSimulation.getOrdersPerSecond());
            userService.addUserOrder(order);
            orderService.addOrder(order);

            matchingEngine.matchOrders();
        }

      /*  Map<Instant, Long> trades = tradePlatform.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTimeStamp, Trade::getPrice));*/
    }
}
