package it.spindox.stagelab.magazzino.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;



//  tramite _DTO:

// impone una struttura JSON stabile e standardizzata per le risposte paginabili delle API, indipendentemente da eventuali modifiche interne alla struttura delle entità o dei repository.


@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SpringDataConfig {
}