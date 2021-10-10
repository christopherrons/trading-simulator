package com.christopher.herron.tradingsimulator.domain.matchingengine;

import com.christopher.herron.tradingsimulator.domain.model.Limit;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.OrderBookEntry;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.domain.orderbook.ReadOnlyOrderBook;
import com.christopher.herron.tradingsimulator.service.TradeService;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class ProRataMatchingAlgorithm extends MatchingAlgorithm {

    private final TradeService tradeService;

    public ProRataMatchingAlgorithm(TradeService tradeService) {
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

        Limit buyLimit = buyLimitIterator.next();
        Limit sellLimit = sellLimitIterator.next();

        OrderBookEntry buyOrderBookEntry = buyLimit.head;
        OrderBookEntry sellOrderBookEntry = sellLimit.head;

        Order buyOrder = buyOrderBookEntry.getOrder();
        Order sellOrder = sellOrderBookEntry.getOrder();

        do {

            if (isMatch(buyOrder, sellOrder, buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity())) {
                addMatchingResults(matchingAlgorithmResults, buyOrder, sellOrder, buyLimit, sellLimit);
            } else {
                break;
            }

            buyOrderBookEntry = getNextOrderBookEntry(buyLimitIterator, buyOrderBookEntry);
            if (buyOrderBookEntry == null) {
                break;
            }
            buyOrder = buyOrderBookEntry.getOrder();


            sellOrderBookEntry = getNextOrderBookEntry(sellLimitIterator, sellOrderBookEntry);
            if (sellOrderBookEntry == null) {
                break;
            }
            sellOrder = sellOrderBookEntry.getOrder();

        } while (true);

        return matchingAlgorithmResults;
    }


    private void addMatchingResults(final MatchingAlgorithmResults matchingAlgorithmResults, final Order buyOrder, final Order sellOrder, final Limit buyLimit, final Limit sellLimit) {
        long quantityTraded = quantityTraded(buyOrder, sellOrder, buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity());
        sellOrder.decreaseQuantity(quantityTraded);
        buyOrder.decreaseQuantity(quantityTraded);

      /*  Trade trade = createTrade(quantityTraded, buyOrder, sellOrder, tradeService);
        matchingAlgorithmResults.addTrade(trade);

        matchingAlgorithmResults.addMatchedOrder(buyOrder);
        matchingAlgorithmResults.addMatchedOrder(sellOrder);

        if (buyOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            matchingAlgorithmResults.addMatchedUserOrderTradePair(buyOrder, trade);
        }

        if (sellOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
            matchingAlgorithmResults.addMatchedUserOrderTradePair(sellOrder, trade);
        }*/
    }

    private boolean isMatch(final Order buyOrder, final Order sellOrder, final long buyLimitTotalQuantity, final long sellLimitTotalQuanity) {
        if (buyOrder.getPrice() < sellOrder.getPrice()) {
            return false;
        }

        if (isBuyOrderTaker(buyOrder, sellOrder)) {
            return (sellOrder.getCurrentQuantity() / (double) sellLimitTotalQuanity) * buyOrder.getCurrentQuantity() >= 1;
        } else {
            return (buyOrder.getCurrentQuantity() / (double) buyLimitTotalQuantity) * sellOrder.getCurrentQuantity() >= 1;
        }
    }

    private long quantityTraded(final Order buyOrder, final Order sellOrder, final long buyLimitTotalQuantity, final long sellLimitTotalQuanity) {
        if (isBuyOrderTaker(buyOrder, sellOrder)) {
            return Math.round((sellOrder.getCurrentQuantity() / (double) sellLimitTotalQuanity) * buyOrder.getCurrentQuantity());
        } else {
            return Math.round((buyOrder.getCurrentQuantity() / (double) buyLimitTotalQuantity) * sellOrder.getCurrentQuantity());
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
