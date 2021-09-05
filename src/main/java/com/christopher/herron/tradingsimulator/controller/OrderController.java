package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.service.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    @GetMapping("/order")
    public String order(Model model) {
        model.addAttribute("order", new Order());
        return "order";
    }

    @PostMapping("/order")
    public String getOrder(@ModelAttribute Order order, Model model) {
        model.addAttribute("order", order);
        return "resultOrder";
    }
}
