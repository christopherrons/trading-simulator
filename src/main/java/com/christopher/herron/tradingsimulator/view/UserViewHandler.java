package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import com.christopher.herron.tradingsimulator.view.model.UserView;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserViewHandler implements ApplicationListener<UpdateUserViewEvent> {

    private final static String USER_TRADES_ENPOINT = "/topic/userTrades";
    private final static String USER_OPEN_ORDERS_ENPOINT = "/topic/openOrders";
    private final static String USER_FILLED_ORDERS_ENPOINT = "/topic/filledOrders";
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, UserView> instrumentIdToUserView = new ConcurrentHashMap<>();

    @Autowired
    public UserViewHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateUserViewEvent updateUserViewEvent) {
        UserView userView;
        if (updateUserViewEvent.getTrade() != null) {
            userView = instrumentIdToUserView.computeIfAbsent(updateUserViewEvent.getTrade().getInstrumentId(), key -> new UserView());
            userView.updateUserTradeTable(updateUserViewEvent.getTrade(), updateUserViewEvent.getOrder().getOrderType());
            updateView(USER_TRADES_ENPOINT, userView.getUserTradeTable());
        } else {
            userView = instrumentIdToUserView.computeIfAbsent(updateUserViewEvent.getOrder().getInstrumentId(), key -> new UserView());
        }

        userView.updateUserOrderTable(updateUserViewEvent.getOrder());
        updateView(USER_FILLED_ORDERS_ENPOINT, userView.getFilledOrderTable());
        updateView(USER_OPEN_ORDERS_ENPOINT, userView.getOpenOrderTable());
    }

    private <T> void updateView(final String endPoint, final List<T> orders) {
        Collections.reverse(orders);
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
    }
}
