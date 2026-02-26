package it.spindox.stagelab.magazzino.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;



//  tramite _DTO:

// impone una struttura JSON stabile e standardizzata per le risposte paginabili delle API


@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SpringDataConfig {
}