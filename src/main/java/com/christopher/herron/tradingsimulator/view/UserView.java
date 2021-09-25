package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

@Component
public class UserView implements ApplicationListener<UpdateUserViewEvent> {

    public final TreeMap<Long, Order> orderIdToOpenOrders = new TreeMap<>();
    public final TreeMap<Long, Order> orderIdToFilledOrders = new TreeMap<>();
    public final TreeMap<Long, Trade> tradeIdToTrade = new TreeMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxUserOrdersInTable = ViewConfigs.getMaxUserOrdersInTable();

    @Autowired
    public UserView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateUserViewEvent updateUserViewEvent) {
        if (updateUserViewEvent.getTrade() == null) {
            updateUserOrderTableView(updateUserViewEvent.getOrder());
        } else {
            updateUserOrderTableView(updateUserViewEvent.getTrade());
        }
    }

    public void updateUserOrderTableView(final Trade trade) {
        updateUserTableView("/topic/userTrades", tradeIdToTrade, trade);
    }

    public void updateUserOrderTableView(final Order order) {
        switch (OrderStatusEnum.fromValue(order.getOrderStatus())) {
            case OPEN:
                updateUserTableView("/topic/openOrders", orderIdToOpenOrders, order);
                break;
            case FILLED:
                updateUserTableView("/topic/filledOrders", orderIdToFilledOrders, order);

                orderIdToOpenOrders.remove(order.getOrderId());
                updateView("/topic/openOrders", new ArrayList<>(orderIdToOpenOrders.values()));
                break;
        }
    }

    private void updateUserTableView(final String endpoint, final TreeMap<Long, Order> orders, final Order order) {
        orders.putIfAbsent(order.getOrderId(), order);
        if (orders.size() > maxUserOrdersInTable) {
            orders.pollFirstEntry();
        }

        updateView(endpoint, new ArrayList<>(orders.values()));
    }

    private void updateUserTableView(final String endpoint, final TreeMap<Long, Trade> trades, final Trade trade) {
        trades.putIfAbsent(trade.getTradeId(), trade);
        if (trades.size() > maxUserOrdersInTable) {
            trades.pollFirstEntry();
        }

        updateView(endpoint, new ArrayList<>(trades.values()));
    }

    private <T> void updateView(final String endPoint, final List<T> orders) {
        Collections.reverse(orders);
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
    }
}

