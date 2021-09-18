package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.domain.utils.TradePlatformUtils;
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
            Order order = generateOrder();
            Thread.sleep(1000 / tradeSimulation.getOrdersPerSecond());
            userService.addOrderToUser(order);
            orderService.addOrder(order);
        }

      /*  Map<Instant, Long> trades = tradePlatform.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTimeStamp, Trade::getPrice));*/
    }

    private Order generateOrder() {
        return Order.valueOf(
                orderBookService.generateOrderId(),
                String.format("Bot: %s", userService.generateUserId()),
                OrderStatusEnum.OPEN.getValue(),
                Instant.now(),
                TradePlatformUtils.generateQuantity(),
                TradePlatformUtils.generatePrice(),
                TradePlatformUtils.getRandomOrderType()
        );
    }
}
