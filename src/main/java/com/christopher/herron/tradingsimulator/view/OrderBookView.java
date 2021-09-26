package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
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

    public final TreeMap<Long, LinkedList<Order>> buyPriceToOpenOrders = new TreeMap<>(Collections.reverseOrder());
    public final TreeMap<Long, LinkedList<Order>> sellPriceToOpenOrders = new TreeMap<>();
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
            updateOrderBookViewAfterTrade(updateOrderBookViewEvent.getBuyOrder(), updateOrderBookViewEvent.getSellOrder());
        } else {
            updateOrderBook(updateOrderBookViewEvent.getNewOrder());
        }
    }

    public void updateOrderBookViewAfterTrade(final Order buyOrder, final Order sellOrder) {
        if (isUpdateIntervalMet() || buyOrder.getUserId().equals(SimulationUtils.getSimulationUser()) || sellOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            createOrderBookDataList();
        }
    }
1
    public void updateOrderBook(final Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
            case SELL:
                sellPriceToOpenOrders.computeIfAbsent(order.getPrice(), key -> new LinkedList<>()).add(order);
                break;
        }

        if (isUpdateIntervalMet() || order.getUserId().equals(SimulationUtils.getSimulationUser())) {
            createOrderBookDataList();
        }
    }

    private void createOrderBookDataList() {
        List<DataTableWrapper<Order>> orderBookDataList = new ArrayList<>();
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(buyPriceToOpenOrders, OrderTypeEnum.BUY)));
        orderBookDataList.add(new DataTableWrapper<>(getTopOrders(sellPriceToOpenOrders, OrderTypeEnum.SELL)));
        update("/topic/orderBook", orderBookDataList);
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


    private void update(String endPoint, List<DataTableWrapper<Order>> orderBookDataList) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orderBookDataList));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }


}

