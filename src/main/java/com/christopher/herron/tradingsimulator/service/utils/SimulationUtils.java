package com.christopher.herron.tradingsimulator.service.utils;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;
import com.christopher.herron.tradingsimulator.common.utils.MathUtils;

public class SimulationUtils {

    public static int getRandomTradeBot(final int upperBound) {
        return MathUtils.generateRandomUniformNumber(0, upperBound);
    }

    public static double generateRandomNormalBuyPrice(double latestTradePrice) {
        return SimulationUtils.generateRandomNormalPrice(latestTradePrice - 2, 3);
    }

    public static double generateRandomNormalSellPrice(double latestTradePrice) {
        return SimulationUtils.generateRandomNormalPrice(latestTradePrice + 2, 3);
    }

    public static double generateRandomNormalPrice(double mean, double standardDeviation) {
        return MathUtils.generateRandomNormalPrice(mean, standardDeviation);
    }

    public static double generateRandomUniformPrice() {
        double lowerBound = 90;
        double upperBound = 110;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static long generateQuantity() {
        long lowerBound = 100;
        long upperBound = 10000;
        return MathUtils.generateRandomUniformNumber(lowerBound, upperBound);
    }

    public static short getRandomOrderType() {
        return MathUtils.generateBoolean() ? OrderTypeEnum.BUY.getValue() : OrderTypeEnum.SELL.getValue();
    }

    public static short getDecimalsInPrice() {
        return 5;
    }

    public static String getSimulationUser() {
        return "ADMIN";
    }

    public static String getSimulationInstrumentId() {
        return "DOGE";
    }
}
