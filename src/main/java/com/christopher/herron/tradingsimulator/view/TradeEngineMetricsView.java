package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.service.OrderBookService;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class TradeEngineMetricsView {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private double previousNumberOfTrades = 0;
    private double previousNumberOfOrders = 0;
    private double previousTradesPerSecond = 0;
    private double previousOrdersPerSecond = 0;
    private Instant previousUpdateTimeStamp = Instant.now();
    private final int millisecondsToSeconds = 1000;

    @Autowired
    public TradeEngineMetricsView(SimpMessagingTemplate messagingTemplate, OrderBookService orderBookService, TradeService tradeService) {
        this.messagingTemplate = messagingTemplate;
        this.orderBookService = orderBookService;
        this.tradeService = tradeService;
    }

    @Scheduled(fixedRate = 2000)
    private void updateMetricsView() {
        long currentNumberOfTrades = tradeService.getTotalNumberOfTrades();
        long currentNumberOfOrders = orderBookService.getTotalNumberOfOrders();

        double tradesPerSecond = valuePerSecond(currentNumberOfTrades, previousNumberOfTrades, previousUpdateTimeStamp);
        double ordersPerSecond = valuePerSecond(currentNumberOfOrders, previousNumberOfOrders, previousUpdateTimeStamp);

        Metric metric = new Metric(
                tradesPerSecond == 0 ? previousNumberOfTrades : tradesPerSecond,
                ordersPerSecond == 0 ? previousOrdersPerSecond : ordersPerSecond,
                currentNumberOfTrades,
                currentNumberOfOrders
        );

        messagingTemplate.convertAndSend("/topic/tradeMetrics", metric);

        previousNumberOfTrades = currentNumberOfTrades;
        previousNumberOfOrders = currentNumberOfOrders;
        previousTradesPerSecond = tradesPerSecond == 0 ? previousTradesPerSecond : tradesPerSecond;
        previousOrdersPerSecond = ordersPerSecond == 0 ? previousOrdersPerSecond : ordersPerSecond;
        previousUpdateTimeStamp = Instant.now();
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

