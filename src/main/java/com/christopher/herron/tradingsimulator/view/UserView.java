package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserView implements ApplicationListener<UpdateUserViewEvent> {

    public final List<Order> openOrders = new ArrayList<>();
    public final List<Order> filledOrders = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final int maxUserOrdersInTable = ViewConfigs.getMaxUserOrdersInTable();
    private final int updateIntervallInMilliseconds = ViewConfigs.getUserViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public UserView(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateUserViewEvent updateUserViewEvent) {
        updateUserTableView(updateUserViewEvent.getOrder());
    }

    public void updateUserTableView(final Order order) {
        switch (OrderStatusEnum.fromValue(order.getOrderStatus())) {
            case OPEN:
                updateUserTableView("/topic/openOrders", openOrders, order);
                break;
            case FILLED:
                updateUserTableView("/topic/filledOrders", filledOrders, order);
                break;
        }
    }

    private void updateUserTableView(final String endpoint, final List<Order> orders, final Order order) {
        orders.add(order);
        if (orders.size() > maxUserOrdersInTable) {
            orders.remove(maxUserOrdersInTable - 1);
        }

        if (isUpdateIntervalMet()) {
            updateView(endpoint, orders);
        }
    }

    private void updateView(final String endPoint, final List<Order> orders) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orders));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }
}

