package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.event.UpdateTradeTableViewEvent;
import com.christopher.herron.tradingsimulator.view.model.TradeTableView;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TradeTableViewHandler implements ApplicationListener<UpdateTradeTableViewEvent> {

    private final static String TRADE_TABLE_ENDPOINT = "/topic/trades";
    private final SimpMessagingTemplate messagingTemplate;
    private final int updateIntervallInMilliseconds = ViewConfigs.getTradeTableViewUpdateIntervallInMilliseconds();
    private final Map<String, TradeTableView> instrumentIdToTradeTableView = new ConcurrentHashMap<>();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public TradeTableViewHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateTradeTableViewEvent updateTradGraphViewEvent) {
        TradeTableView tradeTableView = instrumentIdToTradeTableView.computeIfAbsent(updateTradGraphViewEvent.getTrade().getInstrumentId(), key -> new TradeTableView());
        tradeTableView.updateTradeTableData(updateTradGraphViewEvent.getTrade());
        if (isUpdateIntervalMet()) {
            updateView(TRADE_TABLE_ENDPOINT, tradeTableView.getTrades());
        }
    }

    private void updateView(String endPoint, List<Trade> trades) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(trades));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currentTime = Instant.now().toEpochMilli();
        return currentTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }
}
