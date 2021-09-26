package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.event.UpdateTradGraphViewEvent;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Component
public class TradeGraphViewHandler implements ApplicationListener<UpdateTradGraphViewEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final int updateIntervallInMilliseconds = ViewConfigs.getTradeGraphViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();
    private long intervalAccumelatedPrice = 0;
    private long intervalAccumelatedQuantity = 0;
    private int intervalTotalTrades = 0;
    private long intervalWeightedPrice = 0;

    @Autowired
    public TradeGraphViewHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateTradGraphViewEvent updateTradGraphViewEvent) {
        updateTradeGraphView(updateTradGraphViewEvent.getTrade());
    }

    public void updateTradeGraphView(final Trade trade) {
        updateIntervalValues(trade);

        if (isUpdateIntervalMet()) {
            updateView("/topic/tradeGraph", new TradeDataPoint(calculateIntervalAveragePrice(), calculateVwap(), calculateVwap(), trade.getTimeStamp()));
            resetIntervalValues();
            lastUpdateTime = Instant.now();
        }
    }

    private void updateView(String endPoint, TradeDataPoint tradeDataPoint) {
        messagingTemplate.convertAndSend(endPoint, tradeDataPoint);
    }

    private double calculateVwap() {
        return (double) intervalWeightedPrice / intervalAccumelatedQuantity;
    }

    private double calculateIntervalAveragePrice() {
        return (double) intervalAccumelatedPrice / intervalTotalTrades;
    }


    private void updateIntervalValues(Trade trade) {
        intervalAccumelatedPrice = intervalAccumelatedPrice + trade.getPrice();
        intervalAccumelatedQuantity = intervalAccumelatedQuantity + trade.getQuantity();
        intervalWeightedPrice = intervalWeightedPrice + (trade.getQuantity() * trade.getPrice());
        intervalTotalTrades = intervalTotalTrades + 1;
    }

    private void resetIntervalValues() {
        intervalAccumelatedPrice = 0;
        intervalAccumelatedQuantity = 0;
        intervalTotalTrades = 0;
        intervalWeightedPrice = 0;
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }

    private static class TradeDataPoint {
        private final double price;
        private final double vwap;
        private final double quantity;
        private final Instant timeStamp;

        public TradeDataPoint(double price, double vwap, double quantity, Instant timeStamp) {
            this.price = price;
            this.vwap = vwap;
            this.quantity = quantity;
            this.timeStamp = timeStamp;
        }

        public double getPrice() {
            return price;
        }

        public double getVwap() {
            return vwap;
        }

        public double getQuantity() {
            return this.quantity;
        }

        public String getTimeStamp() {
            SimpleDateFormat formatterHourMinuteSecond = new SimpleDateFormat("HH:mm:ss");
            return formatterHourMinuteSecond.format(Date.from(this.timeStamp));
        }
    }
}

