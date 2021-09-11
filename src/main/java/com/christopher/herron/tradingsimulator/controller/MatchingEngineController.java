package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.domain.Order;
import com.christopher.herron.tradingsimulator.service.MatchingEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Component
public class MatchingEngineController {


    private final MatchingEngineService matchingEngineService;

    @Autowired
    public MatchingEngineController(MatchingEngineService matchingEngineService) {
        this.matchingEngineService = matchingEngineService;
    }

    @GetMapping("/matchingEngine")
    public String orderGetRequest(Model model) {
        model.addAttribute("order", new Order());
        return "index";
    }

    @PostMapping("/matchingEngine")
    public String orderPostRequest(@ModelAttribute Order order, Model model) {
        matchingEngineService.addOrder(order);
        matchingEngineService.matchOrders();
        model.addAttribute("trades", matchingEngineService.getTrades());
        model.addAttribute("buyOrders", matchingEngineService.getBuyOrders());
        model.addAttribute("sellOrders", matchingEngineService.getSellOrders());
        return "index";
    }
}
