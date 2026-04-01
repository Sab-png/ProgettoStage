package it.spindox.stagelab.magazzino.configurations.securityconfigurationssettings;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

public class WebConfiguration {
    private Filter CustomAuthorizationFilter;

    //Configura la Security Filter Chain per l’applicazione

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disabilita protezione CSRF (consigliato per API REST)

                .csrf(AbstractHttpConfigurer::disable)

                // Permette liberamente qualsiasi request (nessuna autenticazione richiesta)

                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // addfilter addpaymentid

                // IL FILTRO CUSTOM AUTHORIZZAZION FILTER

                .addFilterBefore(
                        CustomAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Disabilita form login
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
