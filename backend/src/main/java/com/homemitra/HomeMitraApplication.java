package com.homemitra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HomeMitraApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomeMitraApplication.class, args);
    }
}
