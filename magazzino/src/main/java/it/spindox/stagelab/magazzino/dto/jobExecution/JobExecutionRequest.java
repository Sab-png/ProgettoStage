package it.spindox.stagelab.magazzino.dto.jobExecution;
import io.micrometer.common.util.internal.logging.InternalLogger;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionRequest {


    // FILTRI


    private String nomeJob;            // es. "Inventory Scheduler"
    private String stato;              // es. "SUCCESS", "FAILED"
    private LocalDateTime from;        // data inizio filtraggio
    private LocalDateTime to;          // data fine filtraggio
    private Boolean hasError;          // serve per filtro errori


    // PAGINAZIONE


    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 20;
    private InternalLogger log;


    public Object getStatus() {
        log.info("getStatus() chiamato");
        log.debug("getStatus() attualmente ritorna NULL (metodo non implementato)");

        return null;
    }
}