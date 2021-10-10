package com.christopher.herron.tradingsimulator.common.enumerators;

public enum MatchingAlgorithmEnum {
    NONE("NONE"),

    FIFO("FIFO"),

    PRORATA("PRORATA");

    private final String value;

    MatchingAlgorithmEnum(String value) {
        this.value = value;
    }

    public static MatchingAlgorithmEnum fromValue(String value) {
        switch (value) {
            case "FIFO":
                return FIFO;
            case "PRORATA":
                return PRORATA;
            default:
                return NONE;
        }
    }

    public String getValue() {
        return value;
    }
}