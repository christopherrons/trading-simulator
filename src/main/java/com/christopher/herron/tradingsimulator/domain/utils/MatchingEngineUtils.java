package com.christopher.herron.tradingsimulator.domain.utils;

public class MatchingEngineUtils {

    private long generatedOrderId = 0;

    public long generateOrderId() {
        generatedOrderId = generatedOrderId + 1;
        return generatedOrderId;
    }
}
