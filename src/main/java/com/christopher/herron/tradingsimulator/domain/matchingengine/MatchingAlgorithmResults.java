package com.christopher.herron.tradingsimulator.domain.matchingengine;

import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchingAlgorithmResults {

    private final List<Trade> trades = new ArrayList<>();
    private final List<Pair<Order, Trade>> matchedUserOrderTradePairs = new ArrayList<>();
    private final Set<Order> matchedOrders = new HashSet<>();

    public MatchingAlgorithmResults() {
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void addTrade(final Trade trade) {
        trades.add(trade);
    }

    public List<Pair<Order, Trade>> getMatchedUserOrderTradePairs() {
        return matchedUserOrderTradePairs;
    }

    public void addMatchedUserOrderTradePair(final Order order, final Trade trade) {
        matchedUserOrderTradePairs.add(new ImmutablePair<>(order, trade));
    }

    public Set<Order> getMatchedOrders() {
        return matchedOrders;
    }

    public void addMatchedOrder(final Order order) {
        matchedOrders.add(order);
    }
}
