package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.context.ApplicationEvent;

public class UpdateOrderBookViewEvent extends ApplicationEvent {

    private Order order;

    public UpdateOrderBookViewEvent(Object source, Order newOrder) {
        super(source);
        this.order = newOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
