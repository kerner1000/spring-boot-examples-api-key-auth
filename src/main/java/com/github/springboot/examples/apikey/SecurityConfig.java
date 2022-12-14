package com.github.springboot.examples.apikey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Autowired
    ApiKeyAuthFilter apiKeyAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilterBefore(apiKeyAuthFilter, BasicAuthenticationFilter.class).authorizeRequests().anyRequest().authenticated();
        return http.build();
    }
}
