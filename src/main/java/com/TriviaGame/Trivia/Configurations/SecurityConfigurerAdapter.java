package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@Configuration
public class SecurityConfigurerAdapter {

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Configuración de usuarios en memoria (solo para propósitos de ejemplo)
        	auth.inMemoryAuthentication()
            .withUser("user")
            .password("userTest32@")
            .roles("USER");
    }

      
    protected void configure(HttpSecurity http) throws Exception {
            // Configuración de autorización
        	http.authorizeRequests()
            .requestMatchers("/public/**").permitAll()  // Rutas públicas
            .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
            .and()
            .formLogin().permitAll()  // Configuración de inicio de sesión
            .and()
            .logout().permitAll();  // Configuración de cierre de sesión
       
    }
}