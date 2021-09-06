package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.domain.Order;
import com.christopher.herron.tradingsimulator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Component
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String order(Model model) {
        model.addAttribute("orders", new Order());
        return "order";
    }

    @PostMapping("/orders")
    public String getOrder(@ModelAttribute Order order, Model model) {
        orderService.addOrder(order);
        model.addAttribute("buyOrders", orderService.getBuyOrders());
        model.addAttribute("sellOrders", orderService.getSellOrders());
        return "result";
    }
}
