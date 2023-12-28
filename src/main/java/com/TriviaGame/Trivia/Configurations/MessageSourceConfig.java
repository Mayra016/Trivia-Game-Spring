package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        // Root to message files without language indications
        messageSource.setBasename("classpath:messages");
        
        // Codification of the message files
        messageSource.setDefaultEncoding("UTF-8");
        
        // Time to load files
        messageSource.setCacheSeconds(3600);

        return messageSource;
    }
}
