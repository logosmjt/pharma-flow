package com.pharma.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.pharma.flow.adapter.persistence.repository")
public class PharmaFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmaFlowApplication.class, args);
    }
}
