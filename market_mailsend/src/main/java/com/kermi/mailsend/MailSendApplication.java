package com.kermi.mailsend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MailSendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailSendApplication.class, args);
    }
}
