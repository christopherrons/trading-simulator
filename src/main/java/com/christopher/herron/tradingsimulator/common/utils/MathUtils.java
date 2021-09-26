package com.christopher.herron.tradingsimulator.common.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    private final static Random randomGenerator = new Random();
    public static final double[] ROUNDING_FACTORS = {1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0};
    public static final int[] ADDING_FACTORS = {1, 10, 100, 1000, 10000, 100000};

    public static double generateRandomNormalPrice(final double mean, final double standardDeviation) {
        return randomGenerator.nextGaussian() * standardDeviation + mean;
    }

    public static long generateRandomUniformNumber(final long lowerBound, final long upperBound) {
        return ThreadLocalRandom.current().nextLong(lowerBound, upperBound + 1);
    }

    public static double generateRandomUniformNumber(final double lowerBound, final double upperBound) {
        return ThreadLocalRandom.current().nextDouble(lowerBound, upperBound + 1);
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

    public static double convertToDouble(final long value, final int decimals) {
        return value / ROUNDING_FACTORS[decimals];
    }

    public static double convertToDouble(final double value, final int decimals) {
        return value / ROUNDING_FACTORS[decimals];
    }
}

