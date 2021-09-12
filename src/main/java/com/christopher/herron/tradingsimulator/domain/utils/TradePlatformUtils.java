package com.christopher.herron.tradingsimulator.domain.utils;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderTypeEnum;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TradePlatformUtils {
    private final static Random randomGenerator = new Random();

    public static long generateRandomNormalPrice(long mean, long standardDeviation) {
        return (long) randomGenerator.nextGaussian() * standardDeviation + mean;
    }

    public static long generatePrice() {
        int min = 90;
        int max = 110;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static long generateQuantity() {
        int min = 100;
        int max = 1000;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static short getRandomOrderType() {
        return randomGenerator.nextBoolean() ? OrderTypeEnum.BUY.getValue() : OrderTypeEnum.SELL.getValue();
    }
}
