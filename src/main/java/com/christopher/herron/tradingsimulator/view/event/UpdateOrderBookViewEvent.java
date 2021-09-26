package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.context.ApplicationEvent;

public class UpdateOrderBookViewEvent extends ApplicationEvent {

    private Order newOrder;
    private Order buyOrder;
    private Order sellOrder;

    public UpdateOrderBookViewEvent(Object source, Order newOrder) {
        super(source);
        this.newOrder = newOrder;
    }

    public UpdateOrderBookViewEvent(Object source, Order buyOrder, Order sellOrder) {
        super(source);
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
    }

    public Order getNewOrder() {
        return newOrder;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }
}
