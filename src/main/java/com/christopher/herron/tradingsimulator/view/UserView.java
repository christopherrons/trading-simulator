package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserView {

    public final List<Order> openOrders = new ArrayList<>();
    public final List<Order> filledOrders = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxUserOrdersInTable = ViewConfigs.getMaxUserOrdersInTable();
    private final int updateIntervallInMilliseconds = ViewConfigs.getUserViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();


    @Autowired
    public UserView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateOpenOrderTableView(final Order order) {
        if (openOrders.size() > maxUserOrdersInTable) {
            openOrders.remove(maxUserOrdersInTable - 1);
        }
        openOrders.add(order);
        updateView("/topic/openOrders", openOrders);
    }

    public void updateFilledOrderTableView(final Order order) {
        if (filledOrders.size() > maxUserOrdersInTable) {
            filledOrders.remove(maxUserOrdersInTable - 1);
        }
        filledOrders.add(order);

        //  updateView("/topic/filledOrders", openOrders);
    }

    private void updateView(String endPoint, List<Order> orders) {
        long currenTime = Instant.now().toEpochMilli();
        if (currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds) {
            messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
            lastUpdateTime = Instant.now();
        }
    }
}

