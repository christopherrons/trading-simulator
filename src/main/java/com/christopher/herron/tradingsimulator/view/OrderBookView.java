package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class OrderBookView {

    public final Map<Long, Order> orderIdToBuyOrders = new ConcurrentHashMap<>();
    public final Map<Long, Order> orderIdToSellOrders = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxOrderbookOrdersInTable = ViewConfigs.getMaxOrderbookOrdersInTable();
    private final int updateIntervallInMilliseconds = ViewConfigs.getOrderBookViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public OrderBookView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder) {
        if (buyOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
            orderIdToBuyOrders.remove(buyOrder.getOrderId());
        }

        if (sellOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
            orderIdToSellOrders.remove(sellOrder.getOrderId());
        }
        createOrderBookDataList();
    }

    public void updateOrderBook(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                orderIdToBuyOrders.putIfAbsent(order.getOrderId(), order);
                break;
            case SELL:
                orderIdToSellOrders.putIfAbsent(order.getOrderId(), order);
                break;
        }
        createOrderBookDataList();
    }

    private void createOrderBookDataList() {
        List<DataTableWrapper<Order>> orderBookDataList = new ArrayList<>();
        //orderBookDataList.addAll(getTopOrders(orderIdToBuyOrders, OrderTypeEnum.BUY));
        //orderBookDataList.addAll(getTopOrders(orderIdToSellOrders, OrderTypeEnum.SELL));
        DataTableWrapper<Order> a = new DataTableWrapper<>(getTopOrders(orderIdToBuyOrders, OrderTypeEnum.BUY));
        DataTableWrapper<Order> b  = new DataTableWrapper<>(getTopOrders(orderIdToSellOrders, OrderTypeEnum.SELL));
        orderBookDataList.add(a);
        orderBookDataList.add(b);
        update("/topic/orderBook", orderBookDataList);
    }

    private List<Order> getTopOrders(final Map<Long, Order> orderIdToOrders, final OrderTypeEnum orderType) {
        List<Order> orders = orderIdToOrders.values().stream()
                .sorted(Order::compareTo)
                .collect(Collectors.toList());
        switch (orderType) {
            case BUY:
                return orders.size() > maxOrderbookOrdersInTable ? orders.subList(orders.size() - maxOrderbookOrdersInTable, orders.size()) : orders;
            case SELL:
                return orders.size() > maxOrderbookOrdersInTable ? orders.subList(0, maxOrderbookOrdersInTable) : orders;
        }
        return Collections.emptyList();
    }

    private void update(String endPoint, List<DataTableWrapper<Order>> orderBookDataList) {
        long currenTime = Instant.now().toEpochMilli();
        if (currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds) {
            messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orderBookDataList));
            lastUpdateTime = Instant.now();
        }
    }
}

