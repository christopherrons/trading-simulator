package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

@Component
public class UserView implements ApplicationListener<UpdateUserViewEvent> {

    public final TreeMap<Long, Order> orderIdToOpenOrders = new TreeMap<>();
    public final TreeMap<Long, Order> orderIdToFilledOrders = new TreeMap<>();
    public final TreeMap<Long, UserTradeData> tradeIdToTrade = new TreeMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxUserOrdersInTable = ViewConfigs.getMaxUserOrdersInTable();

    @Autowired
    public UserView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateUserViewEvent updateUserViewEvent) {
        if (updateUserViewEvent.getTrade() != null) {
            updateUserOrderTableView(updateUserViewEvent.getTrade(), updateUserViewEvent.getOrder().getOrderType());
        }
        updateUserOrderTableView(updateUserViewEvent.getOrder());
    }

    public void updateUserOrderTableView(final Trade trade, final short orderType) {
        UserTradeData userTradeData = new UserTradeData(trade, OrderTypeEnum.fromValue(orderType));
        tradeIdToTrade.putIfAbsent(orderType == OrderTypeEnum.BUY.getValue() ? trade.getBuyOrderId() : trade.getSellOrderId(), userTradeData);
        updateUserTableView("/topic/userTrades", tradeIdToTrade, userTradeData);
    }

    public void updateUserOrderTableView(final Order order) {
        switch (OrderStatusEnum.fromValue(order.getOrderStatus())) {
            case OPEN:
                orderIdToOpenOrders.putIfAbsent(order.getOrderId(), order);
                updateUserTableView("/topic/openOrders", orderIdToOpenOrders, order);
                break;
            case FILLED:
                orderIdToOpenOrders.remove(order.getOrderId());

                orderIdToFilledOrders.putIfAbsent(order.getOrderId(), order);
                updateUserTableView("/topic/filledOrders", orderIdToFilledOrders, order);

                updateView("/topic/openOrders", new ArrayList<>(orderIdToOpenOrders.values()));
                break;
        }
    }

    private <T> void updateUserTableView(final String endpoint, final TreeMap<Long, T> dataTable, final T data) {
        if (dataTable.size() > maxUserOrdersInTable) {
            dataTable.pollFirstEntry();
        }

        updateView(endpoint, new ArrayList<>(dataTable.values()));
    }

    private <T> void updateView(final String endPoint, final List<T> orders) {
        Collections.reverse(orders);
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
    }

    private static class UserTradeData {

        private final OrderTypeEnum orderType;
        private final long tradeId;
        private final double price;
        private final long quantity;
        private final Instant timeStamp;

        public UserTradeData(Trade trade, OrderTypeEnum orderType) {
            this.orderType = orderType;
            this.tradeId = trade.getTradeId();
            this.price = trade.getPrice();
            this.quantity = trade.getQuantity();
            this.timeStamp = trade.getTimeStamp();
        }

        public OrderTypeEnum getOrderType() {
            return orderType;
        }

        public double getPrice() {
            return price;
        }

        public long getQuantity() {
            return quantity;
        }

        public Instant getTimeStamp() {
            return timeStamp;
        }

        public long getTradeId() {
            return tradeId;
        }
    }
}
