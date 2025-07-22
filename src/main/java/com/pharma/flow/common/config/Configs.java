package com.pharma.flow.common.config;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configs {

    @Bean
    public CommandLineRunner runner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Beans:");
            Arrays.stream(ctx.getBeanDefinitionNames())
                    .filter(name -> name.contains("audit"))
                    .forEach(System.out::println);
        };
    }
}
