package com.TriviaGame.Trivia.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${PORT:8080}")
	private int port;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/favicon.ico").addResourceLocations("classpath:/static/");
    }
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers("/images/favicon.ico");
    }
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(port);
    }

}
