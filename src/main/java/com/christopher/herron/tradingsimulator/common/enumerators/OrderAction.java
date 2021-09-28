package com.christopher.herron.tradingsimulator.common.enumerators;

public enum OrderAction {
    NONE((short) 0),

    ADD((short) 1),

    UPDATE((short) 2),

    DELETE((short) 3);;

    private final short value;

    OrderAction(short value) {
        this.value = value;
    }

    public static OrderAction fromValue(short value) {
        switch (value) {
            case 1:
                return ADD;
            case 2:
                return UPDATE;
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
