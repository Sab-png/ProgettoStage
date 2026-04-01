package it.spindox.stagelab.magazzino.configurations;

import it.spindox.stagelab.magazzino.filters.CheckoutAuthFilter;
import it.spindox.stagelab.magazzino.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {

    private final UserRepository userRepository;

    public WebConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public CheckoutAuthFilter checkoutAuthFilter() {
        return new CheckoutAuthFilter(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .addFilter(checkoutAuthFilter())
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}