package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.context.ApplicationEvent;

public class UpdateUserViewEvent extends ApplicationEvent {

    private Order order;
    private Trade trade;

    public UpdateUserViewEvent(Object source, Order order) {
        super(source);
        this.order = order;

    }
    public UpdateUserViewEvent(Object source, Order order, Trade trade) {
        super(source);
        this.order = order;
        this.trade = trade;
    }

    public Order getOrder() {
        return order;
    }

    public Trade getTrade() {
        return trade;
    }
}
