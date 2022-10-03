package com.lixubo.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Redis202210Application {

    public static void main(String[] args) {
        SpringApplication.run(Redis202210Application.class, args);
    }

}
