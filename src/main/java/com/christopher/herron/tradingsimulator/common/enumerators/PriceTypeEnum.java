package com.christopher.herron.tradingsimulator.common.enumerators;

public enum PriceTypeEnum {
    NONE((short) 0),

    LAST((short) 1),

    BID((short) 2),

    ASK((short) 3);

    private final short value;

    PriceTypeEnum(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static PriceTypeEnum fromValue(short value) {
        switch (value) {
            case 1:
                return LAST;
            case 2:
                return BID;
            case 3:
                return ASK;
            default:
                return NONE;
        }
    }
}
