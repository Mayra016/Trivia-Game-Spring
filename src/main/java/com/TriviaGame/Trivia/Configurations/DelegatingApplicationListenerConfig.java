package com.TriviaGame.Trivia.Configurations;

import org.springframework.boot.context.config.DelegatingApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.DelegatingApplicationListener;

@Configuration
public class DelegatingApplicationListenerConfig {

    @Bean(name = "miDelegatingApplicationListener")
    public DelegatingApplicationContextInitializer delegatingApplicationListener() {
        return new DelegatingApplicationContextInitializer();
    }
}

