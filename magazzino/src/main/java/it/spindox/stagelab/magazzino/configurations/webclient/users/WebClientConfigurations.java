package it.spindox.stagelab.magazzino.configurations.webclient.users;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Configuration;



@Configuration
public class WebClientConfigurations {

    public WebClientConfigurations() {
        System.out.println(" WebClientConfigurations LOADED");
    }

    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }
}