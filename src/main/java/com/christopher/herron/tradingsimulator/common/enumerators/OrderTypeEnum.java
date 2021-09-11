package com.christopher.herron.tradingsimulator.common.enumerators;

public enum OrderTypeEnum {
    NONE((short) 0),

    BUY((short) 1),

    SELL((short) 2);


    private final short value;

    OrderTypeEnum(short value) {
        this.value = value;
    }

    public static OrderTypeEnum fromValue(short value) {
        switch (value) {
            case 1:
                return BUY;
            case 2:
                return SELL;
            default:
                return NONE;
        }
    }

    public short getValue() {
        return value;
    }
}

