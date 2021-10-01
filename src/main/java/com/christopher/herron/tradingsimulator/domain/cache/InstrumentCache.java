package com.christopher.herron.tradingsimulator.domain.cache;


import com.christopher.herron.tradingsimulator.domain.model.Instrument;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@Component
public class InstrumentCache {

    private final TreeMap<String, Instrument> instrumentIdToinstrument = new TreeMap<>();

    public InstrumentCache() {
    }

    public void addinstrument(final Instrument instrument) {
        instrumentIdToinstrument.putIfAbsent(instrument.getInstrumentId(), instrument);
    }

    public Instrument getInstrument(final String instrumenId) {
        return instrumentIdToinstrument.get(instrumenId);
    }

    public long getTotalNumberOfinstruments() {
        return instrumentIdToinstrument.size();
    }
}
