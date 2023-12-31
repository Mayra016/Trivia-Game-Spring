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

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configuración de usuarios en memoria (solo para propósitos de ejemplo)
        auth.inMemoryAuthentication()
            .withUser("user")
            .password(passwordEncoder().encode("user_GoyaZuleNajwa32@"))
            .roles("ADMIN");
    }

    protected void configure(HttpSecurity http) throws Exception {
        // Configuración de autorización
        http.authorizeRequests()
	        .requestMatchers("/menu", "/{level}", "/languages", "/infos", "/menu/{language}", "/lost").permitAll()
	        .requestMatchers("/levels", "/add/**", "/updateData/**", "/update/**", "/delete/**").hasRole("ADMIN")
	        .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
	        .and()
	        .formLogin()
	            .loginPage("/login")  // Página de inicio de sesión personalizada si es necesario
	            .permitAll()
	        .and()
	        .logout()
	            .logoutUrl("/logout")  // URL de cierre de sesión personalizada si es necesario
	            .logoutSuccessUrl("/login?logout")  // Página de inicio de sesión con mensaje de cierre de sesión
	            .permitAll();

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

