package com.example.pomocnaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PomocnaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PomocnaApplication.class, args);
    }

}
