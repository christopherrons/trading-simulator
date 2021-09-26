package com.christopher.herron.tradingsimulator.domain.model;

public class Instrument {

    private final int instrumentId;
    private final short instrumentType;

    public Instrument(int instrumentId, short instrumentType) {
        this.instrumentId = instrumentId;
        this.instrumentType = instrumentType;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public short getInstrumentType() {
        return instrumentType;
    }
}
