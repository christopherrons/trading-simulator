package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.tradeengine.MatchingEngine;
import org.springframework.stereotype.Service;

@Service
public class MatchingEngineService {

    private final MatchingEngine matchingEngine;

    public MatchingEngineService(MatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    public void runMatchingEngine(final String instrumentId) {
        matchingEngine.matchOrders(instrumentId);
    }
}
