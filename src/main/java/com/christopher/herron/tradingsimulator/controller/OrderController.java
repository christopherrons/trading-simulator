package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.common.utils.DataTableWrapper;
import com.christopher.herron.tradingsimulator.domain.transactions.Order;
import com.christopher.herron.tradingsimulator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

/*    @GetMapping("/tradingEngine")
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

    @MessageMapping
    @SendTo("/topic/order")
    public Order getOrder(HelloMessage message) {
        return Order.valueOf(1L, "chr", (short) 1, Instant.now(), 10L, 20L, (short) 1);
    }*/

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public DataTableWrapper<Greeting> greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!", "test");
        Greeting greetingTwo = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!", "TWO");
        GreetingList greetingList = new GreetingList();
        List<Greeting> hej = new ArrayList<>();
        hej.add(greeting);
        hej.add(greetingTwo);
        DataTableWrapper<Greeting> temp = new DataTableWrapper<>();
        temp.addData(hej);
        return temp;
    }

    @MessageMapping("/orderEntry")
    public void addOrderEntry(Order order) throws Exception {
        orderService.addOrderEntry(order);
    }

    public static class HelloMessage {

        private String name;

        public HelloMessage() {
        }

        public HelloMessage(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Greeting {

        private String kontent;
        private String contentTest;

        public Greeting() {
        }

        public Greeting(String content, String contentTest) {
            this.kontent = content;
            this.contentTest = contentTest;
        }

        public String getKontent() {
            return kontent;
        }

        public String getContentTest() {
            return contentTest;
        }
    }

    public static class GreetingList {
        List<Greeting> greetingsList = new ArrayList<>();

        public GreetingList() {
        }

        public List<Greeting> getGreetingsList() {
            return greetingsList;
        }

        public void addGreeting(Greeting greeting) {
            greetingsList.add(greeting);
        }
    }
}


