package com.christopher.herron.tradingsimulator.service.utils;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.common.utils.MathUtils;

public class SimulationUtils {

    public static int getRandomTradeBot(final int upperBound) {
        return MathUtils.generateRandomUniformNumber(0, upperBound);
    }

    public static long generatePrice() {
        long lowerBound = 90;
        long upperBound = 110;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static long generateQuantity() {
        long lowerBound = 100;
        long upperBound = 1000;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static short getRandomOrderType() {
        return MathUtils.generateBoolean() ? OrderTypeEnum.BUY.getValue() : OrderTypeEnum.SELL.getValue();
    }
}
