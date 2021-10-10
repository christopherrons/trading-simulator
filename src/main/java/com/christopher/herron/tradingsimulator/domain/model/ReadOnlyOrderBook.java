package com.christopher.herron.tradingsimulator.domain.model;

import com.christopher.herron.tradingsimulator.common.enumerators.MatchingAlgorithmEnum;

import java.util.Collection;

public interface ReadOnlyOrderBook {

    Collection<Limit> getBuyOrderBookEntries();

    Collection<Limit> getSellOrderBookEntries();

    MatchingAlgorithmEnum getMatchingAlgorithm();
}
