package com.example.resilience4jratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Resilience4jRateLimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(Resilience4jRateLimiterApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}
