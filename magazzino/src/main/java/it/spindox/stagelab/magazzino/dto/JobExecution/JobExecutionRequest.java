package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionRequest {

    private StatusJob status;

    private LocalDateTime startFrom;
    private LocalDateTime startTo;

    private Boolean hasError;

    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 20;

    public void setNomeJob(String nomeJob) {
    }

    public void setStato(String stato) {
    }

    public void setFrom(LocalDateTime from) {
    }

    public void setTo(LocalDateTime to) {
    }
}

