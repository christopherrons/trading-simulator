package com.christopher.herron.tradingsimulator.common.enumerators;

public enum OrderStatusEnum {
    NONE((short) 0),

    OPEN((short) 1),

    FILLED((short) 2),

    CANCELLED((short) 3);

    private final short value;

    OrderStatusEnum(short value) {
        this.value = value;
    }

    public static OrderStatusEnum fromValue(short value) {
        switch (value) {
            case 1:
                return OPEN;
            case 2:
                return FILLED;
            case 3:
                return CANCELLED;
            default:
                return NONE;
        }
    }

    public short getValue() {
        return value;
    }
}
