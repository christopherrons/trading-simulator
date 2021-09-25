package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.service.OrderBookService;
import com.christopher.herron.tradingsimulator.service.SimulationService;
import com.christopher.herron.tradingsimulator.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@EnableScheduling
@Component
public class TradeEngineMetricsView {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private final SimulationService simulationService;
    private double previousNumberOfTrades = 0;
    private double previousNumberOfOrders = 0;
    private Instant lastUpdateTime = Instant.now();
    private final int millisecondsToSeconds = 1000;

    @Autowired
    public TradeEngineMetricsView(SimpMessagingTemplate messagingTemplate, OrderBookService orderBookService, TradeService tradeService, SimulationService simulationService) {
        this.messagingTemplate = messagingTemplate;
        this.orderBookService = orderBookService;
        this.tradeService = tradeService;
        this.simulationService = simulationService;
    }

    @Scheduled(fixedRate = 2000)
    private void updateMetricsView() {

        long currentNumberOfOrders = orderBookService.getTotalNumberOfOrders();
        if (currentNumberOfOrders != simulationService.getTradeSimulationOrdersToGenerate()) {
            long currentNumberOfTrades = tradeService.getTotalNumberOfTrades();
            double tradesPerSecond = valuePerSecond(currentNumberOfTrades, previousNumberOfTrades, lastUpdateTime);
            double ordersPerSecond = valuePerSecond(currentNumberOfOrders, previousNumberOfOrders, lastUpdateTime);

            Metric metric = new Metric(
                    tradesPerSecond,
                    ordersPerSecond,
                    currentNumberOfTrades,
                    currentNumberOfOrders
            );

            messagingTemplate.convertAndSend("/topic/tradeMetrics", metric);

            previousNumberOfTrades = currentNumberOfTrades;
            previousNumberOfOrders = currentNumberOfOrders;
            lastUpdateTime = Instant.now();
        }
    }

    private double valuePerSecond(double currentNumberOfTrades, double previousNumberOfTrades, Instant lastUpdate) {
        return (currentNumberOfTrades - previousNumberOfTrades) / (Instant.now().toEpochMilli() - lastUpdate.toEpochMilli()) * millisecondsToSeconds;
    }

    private static class Metric {

        private final double tradesPerSecond;
        private final double ordersPerSecond;
        private final long tradesMatched;
        private final long ordersGenerated;

        public Metric(double tradesPerSecond, double ordersPerSecond, long tradesMatched, long ordersGenerated) {
            this.tradesPerSecond = tradesPerSecond;
            this.ordersPerSecond = ordersPerSecond;
            this.tradesMatched = tradesMatched;
            this.ordersGenerated = ordersGenerated;
        }

        public double getTradesPerSecond() {
            return tradesPerSecond; //TODO: Fix rounding
        }

        public double getOrdersPerSecond() {
            return ordersPerSecond;
        }

        public long getTradesMatched() {
            return tradesMatched;
        }

        public long getOrdersGenerated() {
            return ordersGenerated;
        }
    }

}

