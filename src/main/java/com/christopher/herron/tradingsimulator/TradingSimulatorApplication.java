package com.christopher.herron.tradingsimulator;

import com.christopher.herron.tradingsimulator.service.InitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableWebSocketMessageBroker
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(TradingSimulatorApplication.class, args);
        InitService initService = applicationContext.getBean(InitService.class);
        initService.init();
    }

}
