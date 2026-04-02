package it.spindox.stagelab.magazzino.configurations.securityconfigurationssettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfiguration {


    private final CustomAuthorizationFilter customAuthorizationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // per l' uso e per il testing delle API il CSRF deve essere disabilitato
                .csrf(AbstractHttpConfigurer::disable)

                //  le request sono senza filtro  (il filtri in seguito  decideranno quale bloccare)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // Filtro authorizzazion
                //
                .addFilterBefore(
                        customAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Disabilita tutti i meccanismi di login form default
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
