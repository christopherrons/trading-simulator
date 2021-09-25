package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SimulationService {

    private final OrderBookService orderBookService;
    private final UserService userService;
    private final OrderService orderService;
    private TradeSimulation tradeSimulation;

    @Autowired
    public SimulationService(OrderBookService orderBookService, UserService userService, OrderService orderService) {
        this.orderBookService = orderBookService;
        this.userService = userService;
        this.orderService = orderService;
    }

    public void runSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        this.tradeSimulation = tradeSimulation;
        double sleepTime = 1000D / tradeSimulation.getOrdersPerSecond();
        for (int i = 0; i < tradeSimulation.getOrdersToGenerate(); i++) {
            final long generatationStart = Instant.now().toEpochMilli();
            Order order = generateOrder();
            orderService.addOrder(order);
            final long generationTime = Instant.now().toEpochMilli() - generatationStart;

            if (sleepTime - generationTime > 0) {
                Thread.sleep((long) (sleepTime - generationTime));
            }
        }
    }

    private Order generateOrder() {
        return Order.valueOf(
                orderBookService.generateOrderId(),
                String.format("Bot: %s", userService.generateUserId()), //TODO: Create a pool of bots
                OrderStatusEnum.OPEN.getValue(),
                Instant.now(),
                SimulationUtils.generateQuantity(),
                SimulationUtils.generatePrice(),
                SimulationUtils.getRandomOrderType()
        );
    }

    public int getTradeSimulationOrdersToGenerate() {
        return tradeSimulation.getOrdersToGenerate();
    }
}
