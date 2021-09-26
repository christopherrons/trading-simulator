package com.christopher.herron.tradingsimulator.common.enumerators;

public enum InstrumentType {
    NONE((short) 0),
    EQUITY((short) 1),
    OPTION((short) 2),
    FUTURE((short) 3);

    private final short value;

    InstrumentType(short value) {
        this.value = value;
    }

    public static InstrumentType fromValue(short value) {
        switch (value) {
            case 1:
                return EQUITY;
            case 2:
                return OPTION;
            case 3:
                return FUTURE;
            default:
                return NONE;
        }
    }

    public short getValue() {
        return value;
    }
}
