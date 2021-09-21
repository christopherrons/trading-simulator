package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeView {

    public final List<Trade> trades = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxTradesInTable = 10;
    private final int updateIntervallInMilliseconds = 1000;
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public TradeView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateTradeTableView(final Trade trade) {
        if (trades.size() > maxTradesInTable) {
            trades.remove(maxTradesInTable - 1);
        }
        trades.add(0, trade);

        updateView("/topic/trades", trades);
    }

    private void updateView(String endPoint, List<Trade> trades) {
        long currenTime = Instant.now().toEpochMilli();
        if (currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds) {
            messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(trades));
            lastUpdateTime = Instant.now();
        }
    }
}
