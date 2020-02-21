package com.kermi.shoppingcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ShoppingCarApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCarApplication.class, args);
    }
}
