package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


//TODO: Final code analysis and stresstesting

@SpringBootApplication
@EnableWebSocketMessageBroker
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }
}
