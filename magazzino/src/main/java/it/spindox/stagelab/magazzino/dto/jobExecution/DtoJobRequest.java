package it.spindox.stagelab.magazzino.dto.jobExecution;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoJobRequest {

    // FILTRI

    private String nomeJob;            // es. "Inventory Scheduler"
    private String stato;              // es. "SUCCESS", "FAILED"
    private LocalDateTime from;        // data inizio filtraggio (local time)
    private LocalDateTime to;          // data fine filtraggio (local time)
    private Boolean hasError;          // true: solo con errori; false: solo senza; null: tutti

    // PAGINAZIONE
    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 20;
}