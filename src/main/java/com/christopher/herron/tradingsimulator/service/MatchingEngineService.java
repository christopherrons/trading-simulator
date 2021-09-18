package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.tradeplatform.MatchingEngine;
import org.springframework.stereotype.Component;

@Component
public class MatchingEngineService {

    private final MatchingEngine matchingEngine;

    public MatchingEngineService(MatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    public void runMatchingEngine() {
        matchingEngine.matchOrders();
    }
}
