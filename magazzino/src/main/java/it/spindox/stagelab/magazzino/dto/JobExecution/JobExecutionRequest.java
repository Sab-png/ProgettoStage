package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobExecutionRequest {

    private String jobName;
    private StatusJob status;
    private LocalDateTime startFrom;
    private LocalDateTime startTo;
    private Boolean hasError;

    @Min(0)
    private Integer page;

    @Min(1)
    private Integer size;
}
