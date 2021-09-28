package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableWebSocketMessageBroker
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }
}
