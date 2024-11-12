package org.example.xpresbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class XpresBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(XpresBankApplication.class, args);
    }

}
