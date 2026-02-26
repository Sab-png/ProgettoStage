package it.spindox.stagelab.magazzino.dto.jobExecution;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionSearchRequest {

    // FILTRI (tutti opzionali)

    private String stato;
    private LocalDateTime from;
    private LocalDateTime to;       // data/ora fine
    private Boolean hasError;       // TRUE: solo job con errori | FALSE: solo senza | NULL: tutti

    // PAGINAZIONE
    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 20;
}