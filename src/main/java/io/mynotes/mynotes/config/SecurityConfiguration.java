package io.mynotes.mynotes.config;

import io.mynotes.mynotes.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private AuthFilter authFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/authenticate", "/v1/register")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterAt(authFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    UserDetailsService emptyDetailsService() {
        // https://stackoverflow.com/a/75957153/4658283
        return username -> {
            throw new UsernameNotFoundException("No users, only JWTs");
        };
    }
}
