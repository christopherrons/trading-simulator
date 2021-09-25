package com.christopher.herron.tradingsimulator.view.event;

import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.springframework.context.ApplicationEvent;

public class UpdateTradGraphViewEvent extends ApplicationEvent {

    private final Trade trade;

    public UpdateTradGraphViewEvent(Object source, Trade trade) {
        super(source);
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }
}
