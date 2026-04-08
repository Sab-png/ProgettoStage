package it.spindox.stagelab.magazzino.configurations.securityconfigurationssettings;
import it.spindox.stagelab.magazzino.services.utentiservice.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;







@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebConfiguration {

    //  utenti dal DB
    // user o administrator

    private final UserDetailsServiceImpl customUserDetailsService;

    //  filtro per l' addpayment

    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF disabilitato per utilizzo e test di API
                .csrf(AbstractHttpConfigurer::disable)

                //utilizzera' per caricare le info degli utenti dal DB

                .userDetailsService(customUserDetailsService)

                //  Gestisce le autorizzazioni

                .authorizeHttpRequests(auth -> auth

                        // limita la search di fatture solo all' admin

                        .requestMatchers("/fatture/search").hasRole("ADMIN")
                        // le altre request sono libere

                        .anyRequest().permitAll()
                )

                // solo per /fatture/search  richiede l' autenticazione con basic auth

                .httpBasic(Customizer.withDefaults())

                // Aggiunge  il filtro custom prima dello UsernamePasswordAuthenticationFilter
                .addFilterBefore(
                        customAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                //  toglie il login form HTML di spring

                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
