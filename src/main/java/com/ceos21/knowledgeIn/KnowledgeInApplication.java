package com.ceos21.knowledgeIn;

import com.ceos21.knowledgeIn.security.auth.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class KnowledgeInApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeInApplication.class, args);
	}

}
