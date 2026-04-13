package it.spindox.stagelab.magazzino.configurations;

import it.spindox.stagelab.magazzino.filters.CheckoutAuthFilter;
import it.spindox.stagelab.magazzino.repositories.UserRepository;
import it.spindox.stagelab.magazzino.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    public WebConfig(UserRepository userRepository,
                     CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public CheckoutAuthFilter checkoutAuthFilter() {
        return new CheckoutAuthFilter(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password in chiaro
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(authz -> authz
                        // Login libero
                        .requestMatchers("/api/auth/login").permitAll()
                        // searchFattura riservata ad ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/fatture/search").hasRole("ADMIN")
                        // tutto il resto libero
                        .anyRequest().permitAll()
                )
                .addFilter(checkoutAuthFilter())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}