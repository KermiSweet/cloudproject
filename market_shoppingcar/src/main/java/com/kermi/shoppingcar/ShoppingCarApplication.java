package com.kermi.shoppingcar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.RedisUtil;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.kermi.common.mapper")
public class ShoppingCarApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCarApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public RedisUtil getRedisUtil(){
        return new RedisUtil();
    }
}
