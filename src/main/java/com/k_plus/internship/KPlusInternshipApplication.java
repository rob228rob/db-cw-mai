package com.k_plus.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KPlusInternshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(KPlusInternshipApplication.class, args);
    }

}
