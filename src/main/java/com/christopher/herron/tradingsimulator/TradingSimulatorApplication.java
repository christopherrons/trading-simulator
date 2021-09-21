package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

//FIx how settlement price is set
//TODO: Fix Struct of html and CSS
//TODO: Look at MVC pattern
//TODO: Switch from user to user
//TODO: Add task/threads for view updates

@SpringBootApplication
@EnableWebSocketMessageBroker
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }

}
