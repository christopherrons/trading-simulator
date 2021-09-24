package com.christopher.herron.tradingsimulator.common.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    private final static Random randomGenerator = new Random();

    public static long generateRandomNormalPrice(long mean, long standardDeviation) {
        return (long) randomGenerator.nextGaussian() * standardDeviation + mean;
    }

    public static long generateRandomUniformNumber(long lowerBound, long upperBound) {
        return ThreadLocalRandom.current().nextLong(lowerBound, upperBound + 1);
    }

    public static boolean generateBoolean() {
        return randomGenerator.nextBoolean();
    }
}

