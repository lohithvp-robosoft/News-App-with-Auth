package com.example.newsappwithauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsappwithauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsappwithauthApplication.class, args);
	}

}
