package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.InstrumentCache;
import com.christopher.herron.tradingsimulator.domain.model.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentService {
    private final InstrumentCache instrumentCache;

    @Autowired
    public InstrumentService(InstrumentCache instrumentCache) {
        this.instrumentCache = instrumentCache;
    }

    public Instrument getInstrument(final String instrumentId) {
        return instrumentCache.getInstrument(instrumentId);
    }

    public void addinstrument(final Instrument instrument) {
        instrumentCache.addinstrument(instrument);
    }

    public long getTotalNumberOfinstruments() {
        return instrumentCache.getTotalNumberOfinstruments();
    }
}
