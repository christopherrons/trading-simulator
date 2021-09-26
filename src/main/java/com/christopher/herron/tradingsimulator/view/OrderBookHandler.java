package com.christopher.herron.tradingsimulator.view;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import com.christopher.herron.tradingsimulator.view.event.UpdateOrderBookViewEvent;
import com.christopher.herron.tradingsimulator.view.model.OrderBookView;
import com.christopher.herron.tradingsimulator.view.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.view.utils.ViewConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class OrderBookHandler implements ApplicationListener<UpdateOrderBookViewEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final static String ORDER_BOOK_ENDPOINT = "/topic/orderBook";
    private final Map<String, OrderBookView> instrumentIdToOrderBookView = new ConcurrentHashMap<>();
    private final int updateIntervallInMilliseconds = ViewConfigs.getOrderBookViewUpdateIntervallInMilliseconds();
    private Instant lastUpdateTime = Instant.now();

    @Autowired
    public OrderBookHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(UpdateOrderBookViewEvent updateOrderBookViewEvent) {
        OrderBookView orderBookView;
        if (updateOrderBookViewEvent.getNewOrder() == null) {
            if (isUpdateIntervalMet() || updateOrderBookViewEvent.getBuyOrder().getUserId().equals(SimulationUtils.getSimulationUser()) || updateOrderBookViewEvent.getSellOrder().getUserId().equals(SimulationUtils.getSimulationUser())) {
                orderBookView = instrumentIdToOrderBookView.computeIfAbsent(updateOrderBookViewEvent.getBuyOrder().getInstrumentId(), key -> new OrderBookView());
                updateView(ORDER_BOOK_ENDPOINT, orderBookView.getOrderBookTable());
            }

        } else {
            orderBookView = instrumentIdToOrderBookView.computeIfAbsent(updateOrderBookViewEvent.getNewOrder().getInstrumentId(), key -> new OrderBookView());
            orderBookView.updateOrderBookTable(updateOrderBookViewEvent.getNewOrder());

            if (isUpdateIntervalMet() || updateOrderBookViewEvent.getNewOrder().getUserId().equals(SimulationUtils.getSimulationUser())) {
                updateView(ORDER_BOOK_ENDPOINT, orderBookView.getOrderBookTable());
            }
        }
    }

    private void updateView(String endPoint, List<DataTableWrapper<Order>> orderBookDataList) {
        messagingTemplate.convertAndSend(endPoint, new DataTableWrapper<>(orderBookDataList));
        lastUpdateTime = Instant.now();
    }

    private boolean isUpdateIntervalMet() {
        long currenTime = Instant.now().toEpochMilli();
        return currenTime - lastUpdateTime.toEpochMilli() > updateIntervallInMilliseconds;
    }
}
