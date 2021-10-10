package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.matchingengine.FifoMatchingAlgorithm;
import com.christopher.herron.tradingsimulator.domain.matchingengine.MatchingAlgorithmResults;
import com.christopher.herron.tradingsimulator.domain.model.ReadOnlyOrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingEngineService {

    private final FifoMatchingAlgorithm fifoMatchingAlgorithm;

    @Autowired
    public MatchingEngineService(TradeService tradeService) {
        this.fifoMatchingAlgorithm = new FifoMatchingAlgorithm(tradeService);
    }

    public MatchingAlgorithmResults runMatchingEngine(final ReadOnlyOrderBook orderBook) {
        switch (orderBook.getMatchingAlgorithm()) {
            case FIFO:
                return fifoMatchingAlgorithm.matchOrders(orderBook);
            default:
                return new MatchingAlgorithmResults();
        }
    }
}
