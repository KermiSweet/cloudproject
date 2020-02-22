package com.kermi.datacheck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.kermi.common.mapper")
public class DataCheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataCheckApplication.class, args);
    }
}
