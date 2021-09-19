package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.model.Order;
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
    private final int MAX_USER_ORDERS_IN_TABLE = 10;
    private final int UPDATE_INTERVALL_IN_MILLISECONDS = 100;
    private Instant lastUpdateTime = Instant.now();


    @Autowired
    public UserView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateOpenOrderTableView(final Order order) {
        if (openOrders.size() > MAX_USER_ORDERS_IN_TABLE) {
            openOrders.remove(MAX_USER_ORDERS_IN_TABLE - 1);
        }
        openOrders.add(order);
        updateView("/topic/openOrders", openOrders);
    }

    public void updateFilledOrderTableView(final Order order) {
        if (filledOrders.size() > MAX_USER_ORDERS_IN_TABLE) {
            filledOrders.remove(MAX_USER_ORDERS_IN_TABLE - 1);
        }
        filledOrders.add(order);

      //  updateView("/topic/filledOrders", openOrders);
    }

    private void updateView(String endPoint, List<Order> orders) {
        long currenTime = Instant.now().toEpochMilli();
        if(currenTime - lastUpdateTime.toEpochMilli() > UPDATE_INTERVALL_IN_MILLISECONDS) {
            messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
            lastUpdateTime = Instant.now();
        }
    }
}

