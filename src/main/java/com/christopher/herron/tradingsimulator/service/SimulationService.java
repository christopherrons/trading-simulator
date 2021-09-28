package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.domain.model.User;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
public class SimulationService {

    private final OrderBookService orderBookService;
    private final UserService userService;
    private final OrderService orderService;
    private final TradeService tradeService;
    private final int nrOfBots = 100;
    private final List<User> tradeBotUsers = new ArrayList<>();

    @Autowired
    public SimulationService(OrderBookService orderBookService, UserService userService, OrderService orderService, TradeService tradeService) {
        this.orderBookService = orderBookService;
        this.userService = userService;
        this.orderService = orderService;
        this.tradeService = tradeService;
    }

    public void runSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        initTradeBots();


        if (tradeSimulation.getOrdersPerSecond() == 0) {
            runFastSimulation(tradeSimulation);
        } else {
            runThrottledSimulation(tradeSimulation);
        }
    }

    private void runFastSimulation(TradeSimulation tradeSimulation) {
        for (int i = 0; i < tradeSimulation.getOrdersToGenerate(); i++) {
            Order order = generateOrder();
            orderService.addOrder(order);
        }
    }

    private void runThrottledSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        double sleepTime = 1000D / tradeSimulation.getOrdersPerSecond();
        for (int i = 0; i < tradeSimulation.getOrdersToGenerate(); i++) {

            final long generatationStart = Instant.now().toEpochMilli();

            Order order = generateOrder();
            orderService.addOrder(order);

            final long generationTime = Instant.now().toEpochMilli() - generatationStart;

            if (sleepTime - generationTime > 0) {
                Thread.sleep((long) sleepTime - generationTime);
            }
        }
    }

    private void initTradeBots() {
        for (int i = 0; i < nrOfBots; i++) {
            User user = new User(String.format("Bot: %s", userService.generateUserId()));
            userService.addUser(user);
            tradeBotUsers.add(user);
        }
    }

    private Order generateOrder() {
        short orderType = SimulationUtils.getRandomOrderType();
        long quantity = SimulationUtils.generateQuantity();
        return Order.valueOf(
                orderBookService.generateOrderId(),
                tradeBotUsers.get(SimulationUtils.getRandomTradeBot(nrOfBots - 1)).getUserId(),
                OrderStatusEnum.OPEN.getValue(),
                Instant.now(),
                quantity,
                quantity,
                orderType == 1 ? SimulationUtils.generateRandomNormalBuyPrice(tradeService.getLatestPrice()) : SimulationUtils.generateRandomNormalSellPrice(tradeService.getLatestPrice()),
                orderType,
                SimulationUtils.getSimulationInstrumentId()
        );
    }
}
