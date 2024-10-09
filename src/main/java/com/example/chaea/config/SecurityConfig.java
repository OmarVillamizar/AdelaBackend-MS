package com.example.chaea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/", "/login", "/docs/**", "/api-docs/**", "/swagger-ui/**", "/health/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                );
        return http.build();
    }

}
