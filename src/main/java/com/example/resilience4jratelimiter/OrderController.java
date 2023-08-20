package com.example.resilience4jratelimiter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
public class OrderController {

    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_SERVICE="orderService";
    private static final String ORDER_SERVICE_CIRCUIT_BREAKER="orderServiceCircuitBreaker";
    private static final String ORDER_SERVICE_RETRY="orderServiceRetry";

    private static int attempts=1;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order-ratelimiter")
    @RateLimiter(name=ORDER_SERVICE,fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<String> createOrder() {

        //Thread.sleep(5000);
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);

        logger.info(LocalDateTime.now()+" Call processing finished= "+Thread.currentThread().getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order-circuitbreaker")
    @CircuitBreaker(name=ORDER_SERVICE_CIRCUIT_BREAKER,fallbackMethod = "circuitBreakerFallback")
    public ResponseEntity<String> createOrderCircuitBreaker() {

        //Thread.sleep(5000);
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);

        logger.info(LocalDateTime.now()+" Call processing finished= "+Thread.currentThread().getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order-retry")
    @Retry(name=ORDER_SERVICE_RETRY,fallbackMethod = "retryFallback")
    public ResponseEntity<String> createOrderRetry() {

        logger.info("item service call attempted ::"+attempts++);
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);

        logger.info(LocalDateTime.now()+" Call processing finished= "+Thread.currentThread().getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<String> circuitBreakerFallback(Exception e)
    {
        return new ResponseEntity<String>("order service is not available ..circuit breaker",HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> rateLimiterFallback(Exception e)
    {
        return new ResponseEntity<String>("order service does not permits further calls --- rate limiter",HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<String>  retryFallback(Exception e)
    {
        attempts=1;
        return new ResponseEntity<String>("order service does not permits further calls --- retry",HttpStatus.SERVICE_UNAVAILABLE);

    }
}
