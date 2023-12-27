package com.TriviaGame.Trivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.TriviaGame.Trivia.Repositories")
@ComponentScan(basePackages = "com.TriviaGame.Trivia")
@EntityScan(basePackages = "com.TriviaGame.Trivia.Entities")
//@EnableScheduling
public class TriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
	}

}
