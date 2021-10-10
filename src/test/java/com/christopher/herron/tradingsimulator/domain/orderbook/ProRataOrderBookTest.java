package com.christopher.herron.tradingsimulator.domain.orderbook;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderActionEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.domain.matchingengine.MatchingAlgorithmResults;
import com.christopher.herron.tradingsimulator.domain.matchingengine.ProRataMatchingAlgorithm;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProRataOrderBookTest {


    @Test
    void MatchOrders_DoubleQuantity_UnevenDistributionOfQuantity() {
        ProRataOrderBook proRataOrderBook = new ProRataOrderBook(SimulationUtils.getSimulationInstrumentId());
        Order buyOrderOne = createOrder(1, 5, 100D, OrderTypeEnum.BUY.getValue());
        Order buyOrderTwo = createOrder(2, 10, 100D, OrderTypeEnum.BUY.getValue());
        Order buyOrderThree = createOrder(3, 15, 100D, OrderTypeEnum.BUY.getValue());
        Order sellOrderOne = createOrder(4, 10, 100D, OrderTypeEnum.SELL.getValue());
        proRataOrderBook.addOrder(buyOrderThree);
        proRataOrderBook.addOrder(buyOrderTwo);
        proRataOrderBook.addOrder(buyOrderOne);

        proRataOrderBook.addOrder(sellOrderOne);

        ProRataMatchingAlgorithm proRataMatchingAlgorithm = new ProRataMatchingAlgorithm(new TradeService());
        MatchingAlgorithmResults matchingAlgorithmResults = proRataMatchingAlgorithm.matchOrders(proRataOrderBook);

        assertEquals(9, new ArrayList<>(matchingAlgorithmResults.getMatchedOrders()).get(0).getCurrentQuantity());
        assertEquals(7, new ArrayList<>(matchingAlgorithmResults.getMatchedOrders()).get(2).getCurrentQuantity());
        assertEquals(4, new ArrayList<>(matchingAlgorithmResults.getMatchedOrders()).get(4).getCurrentQuantity());
    }

    private Order createOrder(final long orderId, final long quantity, final double price, final short orderType) {
        return Order.valueOf(
                orderId,
                SimulationUtils.getSimulationUser(),
                OrderStatusEnum.OPEN.getValue(),
                Instant.now(),
                quantity,
                quantity,
                price,
                orderType,
                SimulationUtils.getSimulationInstrumentId(),
                OrderActionEnum.ADD.getValue()
        );
    }
}