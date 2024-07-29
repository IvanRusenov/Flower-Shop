package com.ivan.Flowers.Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlowersShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowersShopApplication.class, args);
	}

}
