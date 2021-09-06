package com.christopher.herron.tradingsimulator.domain;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBook {

    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public OrderBook() {
    }

    public void addOrder(Order order) {
        switch (OrderTypeEnum.fromValue(order.getOrderType())) {
            case BUY:
                buyOrders.add(order);
                break;
            case SELL:
                sellOrders.add(order);
                break;
            default:
                break;
        }
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
