package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.domain.model.TradeSimulation;
import com.christopher.herron.tradingsimulator.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SimulationController {

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @MessageMapping("/simulationEntry")
    public void runSimulation(TradeSimulation tradeSimulation) throws InterruptedException {
        simulationService.runSimulation(tradeSimulation);
    }
}
