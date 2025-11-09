package com.fierceadventurer.socialaccountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableKafka
@EnableMethodSecurity
public class SocialAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialAccountServiceApplication.class, args);
    }

}
