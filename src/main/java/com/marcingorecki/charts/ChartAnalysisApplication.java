package com.marcingorecki.charts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChartAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChartAnalysisApplication.class, args);
	}

}
