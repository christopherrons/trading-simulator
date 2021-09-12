package com.christopher.herron.tradingsimulator.controller;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.data.cache.ClientCache;
import com.christopher.herron.tradingsimulator.domain.Order;
import com.christopher.herron.tradingsimulator.domain.Trade;
import com.christopher.herron.tradingsimulator.service.TradingEngineService;
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
import java.util.stream.Collectors;

@Controller
@Component
public class TradingEngineController {

    private final String CLIENT = "CHR";
    private final TradingEngineService matchingEngineService;
    private final ClientCache clientCache;

    @Autowired
    public TradingEngineController(TradingEngineService matchingEngineService, ClientCache clientCache) {
        this.matchingEngineService = matchingEngineService;
        this.clientCache = clientCache;
    }

    @GetMapping("/tradingEngine")
    public String orderGetRequest(Model model) {
        model.addAttribute("order", new Order());
        Map<Date, Long> trades = new LinkedHashMap<>();
        model.addAttribute("tradesGraph", trades);
        return "index";
    }

    @PostMapping("/tradingEngine")
    public String orderPostRequest(@ModelAttribute Order order, Model model) {
        order.setOrderId(matchingEngineService.getOrderNumber() + 1);
        order.setClientId(CLIENT);
        clientCache.addClientOrder(order);

        matchingEngineService.addOrder(order);
        matchingEngineService.matchOrders();

        model.addAttribute("trades", matchingEngineService.getTrades());
        model.addAttribute("buyOrders", matchingEngineService.getBuyOrders());
        model.addAttribute("sellOrders", matchingEngineService.getSellOrders());
        Map<Date, Long> trades = matchingEngineService.getTrades().stream()
                .collect(Collectors.toMap(Trade::getTradeTimeStamp, Trade::getPrice));
        model.addAttribute("tradesGraph", trades);
        model.addAttribute("openOrders", clientCache.getClientOrders(CLIENT, OrderStatusEnum.OPEN));
        //model.addAttribute("filledOrders", clientCache.getClientOrders(CLIENT, OrderStatusEnum.FILLED));

        return "index";
    }
}
