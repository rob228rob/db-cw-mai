package com.mai.db_cw_rbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class LawyerHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawyerHelperApplication.class, args);
    }

}
