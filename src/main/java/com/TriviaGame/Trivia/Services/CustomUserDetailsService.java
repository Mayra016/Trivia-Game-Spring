package com.TriviaGame.Trivia.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:infra/private.properties")
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${USERNAME}")
    private String username;

    @Value("${PASSWORD}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        System.out.println(username + password);
        return User
                .withUsername(username)
                .password(encodedPassword)
                .roles("ADMIN")
                .build();
    }
}
