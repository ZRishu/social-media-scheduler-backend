package com.fierceadventurer.schedulerservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableFeignClients(basePackages = "com.fierceadventurer")
@SpringBootApplication
public class SchedulerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("--- CHECKING LOADED BEANS ---");
            if (ctx.containsBean("feignEncoder")) {
                System.out.println("SUCCESS: 'feignEncoder' (Jackson) is LOADED.");
                System.out.println("Class: " + ctx.getBean("feignEncoder").getClass().getName());
            } else {
                System.err.println("FAILURE: 'feignEncoder' bean is MISSING!");
            }
            System.out.println("-----------------------------");
        };
    }

}
