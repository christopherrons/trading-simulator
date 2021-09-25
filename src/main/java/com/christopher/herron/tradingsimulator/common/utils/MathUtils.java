package com.christopher.herron.tradingsimulator.common.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    private final static Random randomGenerator = new Random();
    private static final double[] ROUNDING_FACTORS = {1.0, 10.0, 100.0, 1000.0};

    public static long generateRandomNormalPrice(final long mean, final long standardDeviation) {
        return (long) randomGenerator.nextGaussian() * standardDeviation + mean;
    }

    public static long generateRandomUniformNumber(final long lowerBound, final long upperBound) {
        return ThreadLocalRandom.current().nextLong(lowerBound, upperBound + 1);
    }

    public static int generateRandomUniformNumber(final int lowerBound, final int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
    }

    public static boolean generateBoolean() {
        return randomGenerator.nextBoolean();
    }

    public static double roundDouble(final double value, final int decimals) {
        if (decimals < ROUNDING_FACTORS.length) {
            return Math.round(value * ROUNDING_FACTORS[decimals]) / ROUNDING_FACTORS[decimals];
        }
        return value;
    }
}

