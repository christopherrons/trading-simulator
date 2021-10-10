package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.matchingengine.FifoMatchingAlgorithm;
import com.christopher.herron.tradingsimulator.domain.matchingengine.MatchingAlgorithmResults;
import com.christopher.herron.tradingsimulator.domain.matchingengine.ProRataMatchingAlgorithm;
import com.christopher.herron.tradingsimulator.domain.orderbook.ReadOnlyOrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingEngineService {

    private final FifoMatchingAlgorithm fifoMatchingAlgorithm;
    private final ProRataMatchingAlgorithm proRataMatchingAlgorithm;

    @Autowired
    public MatchingEngineService(FifoMatchingAlgorithm fifoMatchingAlgorithm, ProRataMatchingAlgorithm proRataMatchingAlgorithm) {
        this.fifoMatchingAlgorithm = fifoMatchingAlgorithm;
        this.proRataMatchingAlgorithm = proRataMatchingAlgorithm;
    }

    public MatchingAlgorithmResults runMatchingEngine(final ReadOnlyOrderBook orderBook) {
        switch (orderBook.getMatchingAlgorithm()) {
            case FIFO:
                return fifoMatchingAlgorithm.matchOrders(orderBook);
            case PRORATA:
                return proRataMatchingAlgorithm.matchOrders(orderBook);
            default:
                return new MatchingAlgorithmResults();
        }
    }
}
