package com.ceos21.knowledgein;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnowledgeInApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeInApplication.class, args);
    }

}
