package com.christopher.herron.tradingsimulator.view.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.common.utils.MathUtils;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class UserView {

    public final TreeMap<Long, Order> orderIdToOpenOrders = new TreeMap<>();
    public final TreeMap<Long, Order> orderIdToFilledOrders = new TreeMap<>();
    public final TreeMap<Long, UserTradeData> tradeIdToTrade = new TreeMap<>();
    private final int maxUserOrdersInTable = ViewConfigs.getMaxUserOrdersInTable();

    public UserView() {
    }

    public void updateUserTradeTable(final Trade trade, final short orderType) {
        UserTradeData userTradeData = new UserTradeData(trade, OrderTypeEnum.fromValue(orderType));
        tradeIdToTrade.putIfAbsent(orderType == OrderTypeEnum.BUY.getValue() ? trade.getBuyOrderId() : trade.getSellOrderId(), userTradeData);
        removeExcessData(tradeIdToTrade);
    }

    public void updateUserOrderTable(final Order order) {
        switch (OrderStatusEnum.fromValue(order.getOrderStatus())) {
            case OPEN:
                orderIdToOpenOrders.putIfAbsent(order.getOrderId(), order);
                removeExcessData(orderIdToOpenOrders);
                break;
            case FILLED:
                orderIdToOpenOrders.remove(order.getOrderId());

                orderIdToFilledOrders.putIfAbsent(order.getOrderId(), order);
                removeExcessData(orderIdToFilledOrders);

                break;
        }
    }

    private <T> void removeExcessData(final TreeMap<Long, T> dataTable) {
        if (dataTable.size() > maxUserOrdersInTable) {
            dataTable.pollFirstEntry();
        }
    }

    public List<Order> getOpenOrderTable() {
        return new ArrayList<>(orderIdToOpenOrders.values());
    }

    public List<Order> getFilledOrderTable() {
        return new ArrayList<>(orderIdToFilledOrders.values());
    }

    public List<UserTradeData> getUserTradeTable() {
        return new ArrayList<>(tradeIdToTrade.values());
    }

    private static class UserTradeData {

        private final OrderTypeEnum orderType;
        private final long tradeId;
        private final long price;
        private final long quantity;
        private final Instant timeStamp;
        private final short decimalsInPrice;

        public UserTradeData(Trade trade, OrderTypeEnum orderType) {
            this.orderType = orderType;
            this.tradeId = trade.getTradeId();
            this.price = trade.getPrice();
            this.quantity = trade.getQuantity();
            this.timeStamp = trade.getTimeStamp();
            this.decimalsInPrice = trade.getDecimalsInPrice();
        }

        public OrderTypeEnum getOrderType() {
            return orderType;
        }

        public long getPrice() {
            return price;
        }

        public double getPriceAsDouble() {
            return MathUtils.convertToDouble(this.price, this.decimalsInPrice);
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
