package com.christopher.herron.tradingsimulator.view.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;

import java.util.*;

public class OrderBookView {

    private final static int maxOrderbookOrdersInTable = ViewConfigs.getMaxOrderbookOrdersInTable();
    private final TreeMap<Long, LinkedList<Order>> buyPriceToOpenOrders = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, LinkedList<Order>> sellPriceToOpenOrders = new TreeMap<>();

    public OrderBookView() {
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder) {
        updateOrderBookViewAfterTrade(buyPriceToOpenOrders, buyOrder);
        updateOrderBookViewAfterTrade(sellPriceToOpenOrders, sellOrder);
    }

    private void updateOrderBookViewAfterTrade(final TreeMap<Long, LinkedList<Order>> orders, final Order tradedOrder) {
        Order order = orders.firstEntry().getValue().getFirst();
        if (tradedOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
            orders.firstEntry().getValue().removeFirst();
            if (orders.firstEntry().getValue().isEmpty()) {
                orders.pollFirstEntry();
            }
        } else {
            order.setCurrentQuantity(tradedOrder.getCurrentQuantity());
        }
    }

    public void updateOrderBookTable(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
            case SELL:
                sellPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
        }
    }

    public List<DataTableWrapper<Order>> getOrderBookTable() {
        List<DataTableWrapper<Order>> orderBookDataList = new ArrayList<>();
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(buyPriceToOpenOrders, OrderTypeEnum.BUY)));
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(sellPriceToOpenOrders, OrderTypeEnum.SELL)));
        return orderBookDataList;
    }

    private List<Order> getTopOrders(final TreeMap<Long, LinkedList<Order>> orderIdToOrders, final OrderTypeEnum orderType) {
        List<Order> topOrders = new ArrayList<>();
        for (Map.Entry<Long, LinkedList<Order>> orderIdToOrdersEntrySet : orderIdToOrders.entrySet()) {
            if (areTopOrdersFound(topOrders, orderIdToOrdersEntrySet.getValue())) {
                break;
            }
        }

        if (orderType == OrderTypeEnum.BUY) {
            Collections.reverse(topOrders);
        }
        return topOrders;
    }

    private boolean areTopOrdersFound(List<Order> topOrders, LinkedList<Order> currentOrders) {
        for (Order currentOrder : currentOrders) {
            topOrders.add(currentOrder);
            if (topOrders.size() == maxOrderbookOrdersInTable) {
                return true;
            }
        }
        return false;
    }
}
