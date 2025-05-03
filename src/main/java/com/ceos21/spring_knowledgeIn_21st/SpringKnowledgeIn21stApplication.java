package com.ceos21.spring_knowledgeIn_21st;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringKnowledgeIn21stApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKnowledgeIn21stApplication.class, args);
    }

}