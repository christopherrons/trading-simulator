package com.christopher.herron.tradingsimulator.domain.orderbook;

import com.christopher.herron.tradingsimulator.common.enumerators.MatchingAlgorithmEnum;
import com.christopher.herron.tradingsimulator.domain.model.Limit;

import java.util.Collection;

public interface ReadOnlyOrderBook {

    Collection<Limit> getBuyOrderBookEntries();

    Collection<Limit> getSellOrderBookEntries();

    MatchingAlgorithmEnum getMatchingAlgorithm();
}
