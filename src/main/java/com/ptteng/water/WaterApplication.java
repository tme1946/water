package com.ptteng.water;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WaterApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaterApplication.class, args);
        System.out.println("******** project started ********");
    }
}
