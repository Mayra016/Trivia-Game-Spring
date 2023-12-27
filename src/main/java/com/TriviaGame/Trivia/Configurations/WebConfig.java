package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/favicon.ico").addResourceLocations("classpath:/static/");
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/favicon.ico");
    }

}
