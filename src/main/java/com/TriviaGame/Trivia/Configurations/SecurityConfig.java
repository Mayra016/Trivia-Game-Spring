package com.TriviaGame.Trivia.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.TriviaGame.Trivia.Services.CustomUserDetailsService;


@EnableWebSecurity
@Configuration
@Order(1) 
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:infra/private.properties")
public class SecurityConfig extends WebSecurityConfiguration {
	@Value("${USERNAME}")
	String username;
	@Value("${PASSWORD}")
	String password;

	public SecurityConfig () {
		System.out.println(username==null?"user = null":username + "pass" + password);
	}

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
              .requestMatchers(new AntPathRequestMatcher("/levels")).authenticated()
              .requestMatchers(new AntPathRequestMatcher("/updateData/**")).authenticated()
              .requestMatchers(new AntPathRequestMatcher("/update/**")).authenticated()
              .requestMatchers(new AntPathRequestMatcher("/add/**")).authenticated()
              .requestMatchers(new AntPathRequestMatcher("/delete/**")).authenticated()
              .anyRequest().authenticated())
          .formLogin()
              .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
	          .permitAll()
	          .and()
	      .logout()
	      	  .logoutSuccessUrl("/login?logout")
	          .permitAll()
	          .and()
	      .exceptionHandling()
	      	.accessDeniedPage("/login");


      return http.build();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService());
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	

    
    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }



    @Bean
    private static CorsConfigurationSource corsConfigurationSource() {
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
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}

