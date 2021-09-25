package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

//FIx how settlement price is set
//TODO: Add intrument
//TODO: Change to one orderbook per instrument
//TODO: Add tabs to user data
//TODO: Fix orders per second
//TODO: Fix final web, make sure things are in the right place and javacsript checks inputs + add footer
//TODO: Find bettter way to generate bid/ask prices
//TODO: Add options to simulation form for if possible

@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableAsync
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }
}
