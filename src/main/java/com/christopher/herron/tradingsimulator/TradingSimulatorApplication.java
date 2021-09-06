package com.christopher.herron.tradingsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//TODO: Replace Greetings with Order Controller and HTML
//TODO: Add Ask html div
// TODO Add Bid html div
//TODO: Create Cacheing / DB system
//TODO: Add matching for ask and bid prices
//TODO: Add Last div
//TODO: Add CSS + graph of last prices
@SpringBootApplication
public class TradingSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingSimulatorApplication.class, args);
	}

}
