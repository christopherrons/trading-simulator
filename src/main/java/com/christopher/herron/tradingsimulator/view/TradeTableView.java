package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeTableView {

    public final List<Trade> trades = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxTradesInTable = ViewConfigs.getMaxTradesInTable();
    private final int updateIntervallInMilliseconds = ViewConfigs.getTradeTableViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public TradeTableView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateTradeTableView(final Trade trade) {
        if (trades.size() > maxTradesInTable) {
            trades.remove(maxTradesInTable - 1);
        }
        trades.add(0, trade);

        if (isUpdateIntervalMet()) {
            updateView("/topic/trades", trades);
        }
    }

    private void updateView(String endPoint, List<Trade> trades) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(trades));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }
}
