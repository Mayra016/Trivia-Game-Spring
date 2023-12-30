package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configuración de usuarios en memoria (solo para propósitos de ejemplo)
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("userTest32@")
            .roles("USER");
    }

    @Override
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Para propósitos de ejemplo, se utiliza un codificador sin encriptación
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:tuPuerto"); // Reemplaza con tu dominio y puerto
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

