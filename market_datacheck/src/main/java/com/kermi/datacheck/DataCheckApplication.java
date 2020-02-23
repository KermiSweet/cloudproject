package com.kermi.datacheck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.RedisUtil;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.kermi.common.mapper")
public class DataCheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataCheckApplication.class, args);
    }

    @Bean
    public RedisUtil redisUtil(){
        return new RedisUtil();
    }
}
