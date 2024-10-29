package com.openhub.mpesasimulatordemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class is responsible for defining the security controls of the application.
 * <p>
 * It customizes how requests are authenticated and authorized, and it integrates
 * a filter to handle token-based authentication.
 *
 * @author Thomas Okoyo
 * @version 1.0
 * @since 2024
 */
@Configuration
public class SecurityConfiguration {
    /**
     * This method configures security settings using HttpSecurity object
     *
     * @param http HttpSecurity object
     * @return SecurityFilterChain object spring uses to enforce security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/*").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()).headers(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
