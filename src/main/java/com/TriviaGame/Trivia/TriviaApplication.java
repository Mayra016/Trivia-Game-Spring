package com.TriviaGame.Trivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.nio.charset.Charset;
import java.util.Locale;
import com.TriviaGame.Trivia.Configurations.*;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.TriviaGame.Trivia.Repositories")
@ComponentScan(basePackages = "com.TriviaGame.Trivia")
@EntityScan(basePackages = "com.TriviaGame.Trivia.Entities")
@EnableScheduling
public class TriviaApplication {

	public static void main(String[] args) {        
		SecurityConfig security = new SecurityConfig();
		System.setProperty("file.encoding", "UTF-8");
		System.out.println(security.passwordEncoder().encode("user_GoyaZuleNajwa32@"));
		SpringApplication.run(TriviaApplication.class, args);
		
	}

}
