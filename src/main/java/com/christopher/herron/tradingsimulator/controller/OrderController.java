package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.domain.tradeplatform.TradeSimulator;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Component
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/tradingEngine")
    public String orderGetRequest(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("tradeSimulator", new TradeSimulator());
        Map<Date, Long> trades = new LinkedHashMap<>();
        model.addAttribute("tradesGraph", trades);
        return "index";
    }

    @PostMapping(path = "/tradingEngine", params = {"orderType", "price", "initialQuantity"})
    public String orderPostRequest(@ModelAttribute Order order, Model model) {
        return orderService.orderPostRequest(order, model);
    }

    @PostMapping(path = "/tradingEngine", params = {"ordersToGenerate"})
    public String runSimulationPostRequest(@ModelAttribute TradeSimulator tradeSimulator, Model model) throws InterruptedException {
        return orderService.runSimulationPostRequest(tradeSimulator, model);
    }
}
