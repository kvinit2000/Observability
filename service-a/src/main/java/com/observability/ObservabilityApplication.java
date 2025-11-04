package com.observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Configuration
@EnableAspectJAutoProxy
/*
mvn spring-boot:run
 */
public class ObservabilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ObservabilityApplication.class, args);
    }
}

