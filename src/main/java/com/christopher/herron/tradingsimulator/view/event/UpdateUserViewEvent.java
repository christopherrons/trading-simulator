package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import org.springframework.context.ApplicationEvent;

public class UpdateUserViewEvent extends ApplicationEvent {

    private final Order order;

    public UpdateUserViewEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
