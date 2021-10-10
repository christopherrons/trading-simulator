package com.christopher.herron.tradingsimulator.domain.matchingengine;

import com.christopher.herron.tradingsimulator.domain.model.Limit;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.OrderBookEntry;
import com.christopher.herron.tradingsimulator.domain.orderbook.ReadOnlyOrderBook;
import com.christopher.herron.tradingsimulator.service.TradeService;
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

        if (!isBuyOrderTaker(buyOrder, sellOrder)) {
            matchBuyOrders(buyOrder, sellOrder, buyLimit, sellLimit, buyOrderBookEntry, buyLimitIterator, matchingAlgorithmResults);
        } else {
            matchSellOrders(buyOrder, sellOrder, buyLimit, sellLimit, sellOrderBookEntry, sellLimitIterator, matchingAlgorithmResults);
        }

        return matchingAlgorithmResults;
    }

    private void matchBuyOrders(Order buyOrder, final Order sellOrder, Limit buyLimit, final Limit sellLimit, OrderBookEntry buyOrderBookEntry, final Iterator<Limit> buyLimitIterator, final MatchingAlgorithmResults matchingAlgorithmResults) {
        do {

            if (isMatch(buyOrder, sellOrder, buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity())) {
                addMatchingResults(matchingAlgorithmResults, buyOrder, sellOrder, buyLimit, sellLimit);
            } else {
                break;
            }

            if (isLimitEmpty(buyOrderBookEntry)) {
                buyLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                if (buyLimitIterator.hasNext()) {
                    buyLimit = buyLimitIterator.next();
                    buyOrderBookEntry = buyLimit.head;
                } else {
                    buyOrderBookEntry = null;
                }
            } else {
                buyOrderBookEntry = buyOrderBookEntry.next;
            }

            if (buyOrderBookEntry == null) {
                sellLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                break;
            }
            buyOrder = buyOrderBookEntry.getOrder();

            if (sellOrder.isOrderFilled()) {
                sellLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                break;
            }
        } while (true);
    }

    private void matchSellOrders(final Order buyOrder, Order sellOrder, final Limit buyLimit, Limit sellLimit, OrderBookEntry sellOrderBookEntry, final Iterator<Limit> sellLimitIterator, final MatchingAlgorithmResults matchingAlgorithmResults) {
        do {

            if (isMatch(buyOrder, sellOrder, buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity())) {
                addMatchingResults(matchingAlgorithmResults, buyOrder, sellOrder, buyLimit, sellLimit);
            } else {
                break;
            }

            if (buyOrder.isOrderFilled()) {
                buyLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                sellLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                break;
            }

            if (isLimitEmpty(sellOrderBookEntry)) {
                sellLimit.decreaseLimitQuantity(Math.min(buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity()));
                if (sellLimitIterator.hasNext()) {
                    sellLimit = sellLimitIterator.next();
                    sellOrderBookEntry = sellLimit.head;
                }
            } else {
                sellOrderBookEntry = sellOrderBookEntry.next;
            }

            if (sellOrderBookEntry == null) {
                break;
            }
            sellOrder = sellOrderBookEntry.getOrder();
        } while (true);
    }


    private void addMatchingResults(final MatchingAlgorithmResults matchingAlgorithmResults, final Order buyOrder, final Order sellOrder, final Limit buyLimit, final Limit sellLimit) {
        long quantityTraded = quantityTraded(buyOrder, sellOrder, buyLimit.getTotalLimitQuantity(), sellLimit.getTotalLimitQuantity());
        sellOrder.decreaseQuantity(quantityTraded);
        buyOrder.decreaseQuantity(quantityTraded);

      /*  Trade trade = createTrade(quantityTraded, buyOrder, sellOrder, tradeService);
        matchingAlgorithmResults.addTrade(trade);*/

        matchingAlgorithmResults.addMatchedOrder(buyOrder);
        matchingAlgorithmResults.addMatchedOrder(sellOrder);

        /*   if (buyOrder.getUserId().equals(SimulationUtils.getSimulationUser())) {
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
            return (sellOrder.getCurrentQuantity() / (double) sellLimitTotalQuanity) * buyOrder.getInitialQuantity() >= 1;
        } else {
            return (buyOrder.getCurrentQuantity() / (double) buyLimitTotalQuantity) * sellOrder.getInitialQuantity() >= 1;
        }
    }

    private long quantityTraded(final Order buyOrder, final Order sellOrder, final long buyLimitTotalQuantity, final long sellLimitTotalQuanity) {
        if (isBuyOrderTaker(buyOrder, sellOrder)) {
            return (long) Math.floor((sellOrder.getCurrentQuantity() / (double) sellLimitTotalQuanity) * buyOrder.getInitialQuantity());
        } else {
            return (long) Math.floor((buyOrder.getCurrentQuantity() / (double) buyLimitTotalQuantity) * sellOrder.getInitialQuantity());
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
