package com.christopher.herron.tradingsimulator.domain.utils;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;

public class TradeEngineUtils {

    public static long generatePrice() {
        int lowerBound = 90;
        int upperBound = 110;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static long generateQuantity() {
        int lowerBound = 100;
        int upperBound = 1000;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static short getRandomOrderType() {
        return MathUtils.generateBoolean() ? OrderTypeEnum.BUY.getValue() : OrderTypeEnum.SELL.getValue();
    }
}
