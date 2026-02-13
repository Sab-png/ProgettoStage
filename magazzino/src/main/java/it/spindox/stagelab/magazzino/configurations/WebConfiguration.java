package it.spindox.stagelab.magazzino.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfiguration {

     //Configura la Security Filter Chain per l’applicazione

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disabilita protezione CSRF (consigliato per API REST)

                .csrf(AbstractHttpConfigurer::disable)

                // Permette liberamente qualsiasi request (nessuna autenticazione richiesta)

                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // Disabilita il form login predefinito di Spring Security

                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}