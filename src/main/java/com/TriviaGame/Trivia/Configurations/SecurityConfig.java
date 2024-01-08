package com.TriviaGame.Trivia.Configurations;

import java.io.IOException;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TriviaGame.Trivia.Services.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableWebSecurity
@Configuration
@Order(1) 
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfiguration {
	@Value("${USERNAME}")
	String username;
	@Value("${PASSWORD}")
	String password;
	
	
	@Bean
	public static SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .requiresChannel(channel -> channel.anyRequest().requiresSecure())
	        .authorizeRequests(authorize -> authorize.anyRequest().permitAll())
	        .portMapper().http(80).mapsTo(443)  // Mapea el puerto 80 a 443 para la redirección
	        .and()
	        .addFilterBefore(new RedirectToHttpsFilter(), ChannelProcessingFilter.class);
	        
	      return http.build();
	}
	
    private static class RedirectToHttpsFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if (!request.isSecure()) {
                String redirectUrl = "https://" + request.getServerName() + request.getRequestURI();
                response.sendRedirect(redirectUrl);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
 
    @SuppressWarnings({ "removal", "deprecation" })
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        	.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                    .disable()
                )
            )
        	.cors().configurationSource(corsConfigurationSource())
        	.and()
            .requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure()
	        .and()
	        .authorizeRequests()
	        .and()
	        .portMapper()
	            .http(8080).mapsTo(8443)
	        .and()    
	        .authorizeRequests(requests -> requests
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
        configuration.addAllowedOrigin("http://localhost:8443"); 
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedHeader("Access-Control-Allow-Headers");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("X-Token");

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
    
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080); // Puerto HTTP
        connector.setSecure(false);
        connector.setRedirectPort(8443); // Puerto HTTPS
        return connector;
    }
    

}   
    