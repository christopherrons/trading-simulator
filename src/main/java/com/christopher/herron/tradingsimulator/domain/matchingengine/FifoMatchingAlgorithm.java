package com.christopher.herron.tradingsimulator.domain.matchingengine;

import com.christopher.herron.tradingsimulator.domain.model.*;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class FifoMatchingAlgorithm extends MatchingAlgorithm {

    private final TradeService tradeService;

    public FifoMatchingAlgorithm(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public MatchingAlgorithmResults matchOrders(final ReadOnlyOrderBook orderBook) {
        MatchingAlgorithmResults matchingAlgorithmResults = new MatchingAlgorithmResults();

        Iterator<Limit> buyLimitIterator = orderBook.getBuyOrderBookEntries().iterator();
        Iterator<Limit> sellLimitIterator = orderBook.getSellOrderBookEntries().iterator();

        if (isOneSided(buyLimitIterator, sellLimitIterator)) {
            return matchingAlgorithmResults;
        }

        OrderBookEntry buyOrderBookEntry = buyLimitIterator.next().head;
        OrderBookEntry sellOrderBookEntry = sellLimitIterator.next().head;

        Order buyOrder = buyOrderBookEntry.getOrder();
        Order sellOrder = sellOrderBookEntry.getOrder();

        do {

            if (isMatch(buyOrder.getPrice(), sellOrder.getPrice())) {
                addMatchingResults(matchingAlgorithmResults, buyOrder, sellOrder);
            } else {
                break;
            }

            if (buyOrder.isOrderFilled()) {
                buyOrderBookEntry = getNextOrderBookEntry(buyLimitIterator, buyOrderBookEntry);
                if (buyOrderBookEntry == null) {
                    break;
                }
                buyOrder = buyOrderBookEntry.getOrder();
            }

            if (sellOrder.isOrderFilled()) {
                sellOrderBookEntry = getNextOrderBookEntry(sellLimitIterator, sellOrderBookEntry);
                if (sellOrderBookEntry == null) {
                    break;
                }
                sellOrder = sellOrderBookEntry.getOrder();
            }

        } while (true);

        return matchingAlgorithmResults;
    }


    private void addMatchingResults(final MatchingAlgorithmResults matchingAlgorithmResults, final Order buyOrder, final Order sellOrder) {
        long quantityTraded = quantityTraded(buyOrder.getCurrentQuantity(), sellOrder.getCurrentQuantity());
        sellOrder.decreaseQuantity(quantityTraded);
        buyOrder.decreaseQuantity(quantityTraded);

        Trade trade = createTrade(quantityTraded, buyOrder, sellOrder, tradeService);
        matchingAlgorithmResults.addTrade(trade);

        matchingAlgorithmResults.addMatchedOrder(buyOrder);
        matchingAlgorithmResults.addMatchedOrder(sellOrder);

        if (buyOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            matchingAlgorithmResults.addMatchedUserOrderTradePair(buyOrder, trade);
        }

        if (sellOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            matchingAlgorithmResults.addMatchedUserOrderTradePair(sellOrder, trade);
        }
    }

    private OrderBookEntry getNextOrderBookEntry(final Iterator<Limit> limitIterator, final OrderBookEntry currentOrderBookEntry) {
        if (isLimitEmpty(currentOrderBookEntry)) {
            if (limitIterator.hasNext()) {
                return limitIterator.next().head;
            }
        } else {
            return currentOrderBookEntry.next;
        }
        return null;
    }

    private boolean isLimitEmpty(final OrderBookEntry orderBookEntry) {
        return orderBookEntry.next == null;
    }
}
