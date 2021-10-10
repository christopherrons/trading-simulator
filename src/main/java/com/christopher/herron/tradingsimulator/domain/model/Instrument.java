package com.christopher.herron.tradingsimulator.domain.model;

public class Instrument {

    private final String instrumentId;
    private final short instrumentType;
    private String matchingAlgorithm;

    public Instrument(String instrumentId, short instrumentType, String matchingAlgorithm) {
        this.instrumentId = instrumentId;
        this.instrumentType = instrumentType;
        this.matchingAlgorithm = matchingAlgorithm;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public short getInstrumentType() {
        return instrumentType;
    }

    public String getMatchingAlgorithm() {
        return matchingAlgorithm;
    }

    public void setMatchingAlgorithm(String matchingAlgorithm) {
        this.matchingAlgorithm = matchingAlgorithm;
    }
}
