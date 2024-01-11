package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Configuration
public class SecurityConfigurerAdapter extends WebSecurityConfiguration{

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            @SuppressWarnings("deprecation")
			UserBuilder user = User.withDefaultPasswordEncoder(); 		// Configuración de usuarios en memoria (solo para propósitos de ejemplo)
        	auth.inMemoryAuthentication()
            .withUser(user.username("hola").password("userTest32@").roles("USER"));

    }

      
}