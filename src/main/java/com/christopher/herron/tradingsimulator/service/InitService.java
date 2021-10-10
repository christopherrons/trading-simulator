package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.common.enumerators.InstrumentTypeEnum;
import com.christopher.herron.tradingsimulator.domain.model.Instrument;
import com.christopher.herron.tradingsimulator.service.utils.SimulationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    private final InstrumentService instrumentService;
    private final SimulationService simulationService;

    @Autowired
    public InitService(InstrumentService instrumentService, SimulationService simulationService) {
        this.instrumentService = instrumentService;
        this.simulationService = simulationService;
    }

    public void init() {
        initInstruments();
        initTradeBots();
    }

    private void initInstruments() {
        instrumentService.addinstrument(new Instrument(SimulationUtils.getSimulationInstrumentId(), InstrumentTypeEnum.EQUITY.getValue(), SimulationUtils.getSimulationMatchingAlgorithm()));
    }

    private void initTradeBots() {
        simulationService.initTradeBots();
    }
}
