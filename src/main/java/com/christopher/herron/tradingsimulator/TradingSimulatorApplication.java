package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

//FIx how settlement price is set
//TODO: Fix orders per second
//TODO: Find bettter way to generate bid/ask prices +  Add options to simulation form for if relevant
//TODO: Handle trading in decimals
//TODO: Add about page


@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableAsync
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }
}
