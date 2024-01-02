package com.TriviaGame.Trivia.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@Configuration
@Order(1) 
public class SecurityConfig extends WebSecurityConfiguration {

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configuración de usuarios en memoria (solo para propósitos de ejemplo)
        auth.inMemoryAuthentication()
            .withUser("user")
            .password(passwordEncoder().encode("user_GoyaZuleNajwa32@"))
            .roles("ADMIN");
    }


    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
      http
          .authorizeHttpRequests(requests -> requests
              .requestMatchers(new AntPathRequestMatcher("/menu/**")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/{level}/**")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/lost")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/infos")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/languages")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
              .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
              .anyRequest().authenticated())
          .httpBasic();
      return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Para propósitos de ejemplo, se utiliza un codificador sin encriptación
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://peculiaridadesdelmundo.blogspot.com");
        configuration.addAllowedOrigin("https://peculiaridadesdomundoblog.blogspot.com");
        configuration.addAllowedOrigin("https://besonderheitenderwelt.blogspot.com");
        configuration.addAllowedOrigin("https://peculiaritiesoftheworld.blogspot.com");
        configuration.addAllowedOrigin("http://localhost:8080"); 
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

