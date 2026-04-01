package it.spindox.stagelab.magazzino.configurations.pageableconfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.context.annotation.Bean;



@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)

public class SpringDataConfig {

    // Personalizza i parametri di paginazione
     // nel caso volessi che la prima pagina sia 1 invece di 0

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);
            p.setMaxPageSize(100);           // Limite massimo di record per pagina
        };
    }
}