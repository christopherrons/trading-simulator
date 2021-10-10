package com.christopher.herron.tradingsimulator.common.enumerators;

public enum OrderActionEnum {
    NONE((short) 0),

    ADD((short) 1),

    MODIFY((short) 2),

    DELETE((short) 3);

    private final short value;

    OrderActionEnum(short value) {
        this.value = value;
    }

    public static OrderActionEnum fromValue(short value) {
        switch (value) {
            case 1:
                return ADD;
            case 2:
                return MODIFY;
            case 3:
                return DELETE;
            default:
                return NONE;
        }
    }

    public short getValue() {
        return value;
    }
}
