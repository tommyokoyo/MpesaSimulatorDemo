package com.openhub.mpesasimulatordemo.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final AuthHeaderFilter authHeaderFilter;

    @Autowired
    public SecurityConfig(AuthHeaderFilter authHeaderFilter) {
        this.authHeaderFilter = authHeaderFilter;
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/mpesa-sim/transaction/**").authenticated()
                        .requestMatchers("/mpesa-sim/authentication/**").permitAll()
                        .anyRequest().permitAll())
                .logout(logout -> logout.permitAll());

        http.addFilterBefore(authHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
