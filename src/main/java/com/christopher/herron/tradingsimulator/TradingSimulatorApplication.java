package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//FIx how settlement price is set
//TODO: Create an update controller which updates the view
//TODO: Fix Struct of html and CSS
//TODO: Fix names and OOP design
//TODO: Fix background
//TODO: Look at MVC pattern
//TODO: How to handle trading with yourself
//TODO: Switch from user to user

//Nice to have
//TODO: ADD metrics
//TODO: Create Cacheing / DB systems

@SpringBootApplication
@EnableScheduling
public class TradingSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSimulatorApplication.class, args);
    }

}
