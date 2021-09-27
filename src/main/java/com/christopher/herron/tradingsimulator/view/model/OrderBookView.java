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
    private final TreeMap<Long, LinkedList<Order>> tempBuy = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, LinkedList<Order>> tempBuyRemoved = new TreeMap<>(Collections.reverseOrder());

    private final TreeMap<Long, LinkedList<Order>> sellPriceToOpenOrders = new TreeMap<>();
    private final TreeMap<Long, LinkedList<Order>> tempSell = new TreeMap<>();
    private final TreeMap<Long, LinkedList<Order>> tempSellRemoved = new TreeMap<>();

    public OrderBookView() {
    }

    public void updateOrderBookTable(final Order buyOrder, final Order sellOrder) {
        Order bestBuyOrder = buyPriceToOpenOrders.firstEntry().getValue().getFirst();
        if (buyOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
            Order tempBuy = buyPriceToOpenOrders.firstEntry().getValue().removeFirst();
            tempBuyRemoved.computeIfAbsent(tempBuy.getPrice(), key -> new LinkedList<>()).add(tempBuy.copy());
            if (buyPriceToOpenOrders.firstEntry().getValue().isEmpty()) {
                buyPriceToOpenOrders.pollFirstEntry();
            }
        } else {
            bestBuyOrder.setCurrentQuantity(buyOrder.getCurrentQuantity());
        }

        Order bestSellOrder = sellPriceToOpenOrders.firstEntry().getValue().getFirst();
        if (sellOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
            Order tempSell = sellPriceToOpenOrders.firstEntry().getValue().removeFirst();
            tempSellRemoved.computeIfAbsent(tempSell.getPrice(), key -> new LinkedList<>()).add(tempSell.copy());
            if (sellPriceToOpenOrders.firstEntry().getValue().isEmpty()) {
                sellPriceToOpenOrders.pollFirstEntry();
            }

        } else {
            bestSellOrder.setCurrentQuantity(sellOrder.getCurrentQuantity());
        }
    }

    public void updateOrderBookTable(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                tempBuy.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order.copy());
                break;
            case SELL:
                sellPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                tempSell.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order.copy());
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

        for (Iterator<Map.Entry<Long, LinkedList<Order>>> orderIdToOrdersIterator = orderIdToOrders.entrySet().iterator(); orderIdToOrdersIterator.hasNext(); ) {
            Map.Entry<Long, LinkedList<Order>> orderIdToOrdersEntrySet = orderIdToOrdersIterator.next();

            if (areTopOrdersFound(topOrders, orderIdToOrdersEntrySet.getValue())) {
                break;
            }

            if (orderIdToOrdersEntrySet.getValue().isEmpty()) {
                //   orderIdToOrdersIterator.remove();
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
                //  orderIterator.remove();
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
