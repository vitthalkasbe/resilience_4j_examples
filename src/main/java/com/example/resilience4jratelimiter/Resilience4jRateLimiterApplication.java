package com.example.resilience4jratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class Resilience4jRateLimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(Resilience4jRateLimiterApplication.class, args);
		int i=1;
		//for semaphore bulkhead
//		IntStream.range(i,10).parallel().forEach(t->{
//			String response = new RestTemplate().getForObject("http://localhost:8082/order-bulkhead-semaphore", String.class);
//		});

		//for fixedThreadPool bulkhead
		IntStream.range(i,10).parallel().forEach(t->{
			String response = new RestTemplate().getForObject("http://localhost:8082/order-bulkhead-fixedThreadPool", String.class);
		});


	}

	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}
