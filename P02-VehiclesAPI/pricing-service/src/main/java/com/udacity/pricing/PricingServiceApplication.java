package com.udacity.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * Creates a Spring Boot Application to run the Pricing Service.
 * DONE Converted the application from a REST API to a microservice.
 */
@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
@EnableEurekaClient
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

}
