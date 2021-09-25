package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.context.ApplicationEvent;

public class UpdateOrderBookViewEvent extends ApplicationEvent {

    private Order newOrder;

    public UpdateOrderBookViewEvent(Object source, Order newOrder) {
        super(source);
        this.newOrder = newOrder;
    }

    public UpdateOrderBookViewEvent(Object source) {
        super(source);

    }

    public Order getNewOrder() {
        return newOrder;
    }
}
