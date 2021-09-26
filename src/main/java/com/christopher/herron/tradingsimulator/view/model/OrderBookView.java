package com.christopher.herron.tradingsimulator.view.model;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;

import java.util.*;

public class OrderBookView {

    public final TreeMap<Long, LinkedList<Order>> buyPriceToOpenOrders = new TreeMap<>(Collections.reverseOrder());
    public final TreeMap<Long, LinkedList<Order>> sellPriceToOpenOrders = new TreeMap<>();
    private final static int maxOrderbookOrdersInTable = ViewConfigs.getMaxOrderbookOrdersInTable();

    public OrderBookView() {
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
        for (LinkedList<Order> currentOrders : orderIdToOrders.values()) {
            if (areTopOrdersFound(topOrders, currentOrders)) {
                break;
            }
        }

        if (orderType == OrderTypeEnum.BUY) {
            Collections.reverse(topOrders);
        }
        return topOrders;
    }

    private boolean areTopOrdersFound(List<Order> topOrders, LinkedList<Order> currentOrders) {
        for (Iterator<Order> orderIterator = currentOrders.iterator(); orderIterator.hasNext(); ) {
            Order currentOrder = orderIterator.next();
            if (currentOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
                orderIterator.remove();
            } else {
                topOrders.add(currentOrder);
            }
            if (topOrders.size() == maxOrderbookOrdersInTable) {
                return true;
            }
        }
        return false;
    }
}
