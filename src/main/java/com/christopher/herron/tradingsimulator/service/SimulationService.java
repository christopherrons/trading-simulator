package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.domain.utils.TradeEngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SimulationService {

    private final OrderBookService orderBookService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public SimulationService(OrderBookService orderBookService, UserService userService, OrderService orderService) {
        this.orderBookService = orderBookService;
        this.userService = userService;
        this.orderService = orderService;
    }

    public void runSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        for (int i = 0; i < tradeSimulation.getOrdersToGenerate(); i++) {
            long generatationStart = Instant.now().toEpochMilli();
            Order order = generateOrder();
            orderService.addOrder(order);
            long generatationEnd = Instant.now().toEpochMilli();
            Thread.sleep((1000 - (generatationEnd - generatationStart)) / tradeSimulation.getOrdersPerSecond());
        }

      /*  Map<Instant, Long> trades = tradePlatform.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTimeStamp, Trade::getPrice));*/
    }

    private Order generateOrder() {
        return Order.valueOf(
                orderBookService.generateOrderId(),
                String.format("Bot: %s", userService.generateUserId()), //TODO: Create a pool of bots
                OrderStatusEnum.OPEN.getValue(),
                Instant.now(),
                TradeEngineUtils.generateQuantity(),
                TradeEngineUtils.generatePrice(),
                TradeEngineUtils.getRandomOrderType()
        );
    }
}
