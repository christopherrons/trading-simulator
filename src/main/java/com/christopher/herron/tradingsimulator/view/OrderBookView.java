package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.event.UpdateOrderBookViewEvent;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;


@Component
public class OrderBookView implements ApplicationListener<UpdateOrderBookViewEvent> {

    public final TreeMap<Long, LinkedList<Order>> buyPriceToOrders = new TreeMap<>(Collections.reverseOrder());
    public final TreeMap<Long, LinkedList<Order>> sellPriceToOrders = new TreeMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxOrderbookOrdersInTable = ViewConfigs.getMaxOrderbookOrdersInTable();
    private final int updateIntervallInMilliseconds = ViewConfigs.getOrderBookViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public OrderBookView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateOrderBookViewEvent updateOrderBookViewEvent) {
        if (updateOrderBookViewEvent.getNewOrder() == null) {
            updateOrderBookViewAfterTrade();
        } else {
            updateOrderBook(updateOrderBookViewEvent.getNewOrder());
        }
    }

    public void updateOrderBookViewAfterTrade() {
        if (isUpdateIntervalMet()) {
            createOrderBookDataList();
        }
    }

    public void updateOrderBook(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
            case SELL:
                sellPriceToOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
        }

        if (isUpdateIntervalMet()) {
            createOrderBookDataList();
        }
    }

    private void createOrderBookDataList() {
        List<DataTableWrapper<Order>> orderBookDataList = new ArrayList<>();
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(buyPriceToOrders, OrderTypeEnum.BUY)));
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(sellPriceToOrders, OrderTypeEnum.SELL)));
        update("/topic/orderBook", orderBookDataList);
    }

    private List<Order> getTopOrders(final TreeMap<Long, LinkedList<Order>> orderIdToOrders, final OrderTypeEnum orderType) {
        List<Order> orders = new ArrayList<>();
        boolean topOrdersFound = false;
        for (LinkedList<Order> currentOrders : orderIdToOrders.values()) {
            if (topOrdersFound) {
                break;
            }
            for (Iterator<Order> orderIterator = currentOrders.iterator(); orderIterator.hasNext(); ) {
                Order currentOrder = orderIterator.next();
                if (currentOrder.getOrderStatus() == OrderStatusEnum.FILLED.getValue()) {
                    orderIterator.remove();
                } else {
                    orders.add(currentOrder);
                }
                if (orders.size() == maxOrderbookOrdersInTable) {
                    topOrdersFound = true;
                    break;
                }
            }
        }

        if (orderType == OrderTypeEnum.BUY) {
            Collections.reverse(orders);
        }
        return orders;
    }

    private void update(String endPoint, List<DataTableWrapper<Order>> orderBookDataList) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orderBookDataList));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }


}

