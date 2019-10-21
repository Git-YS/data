package com.hrms.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class DataApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }

}
