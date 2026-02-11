package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class JobExecutionRequest {

    private StatusJob status;
    private LocalDateTime startFrom;
    private LocalDateTime startTo;
    private Boolean hasError;

    @Min(0)
    private Integer page;

    @Min(1)
    private Integer size;
}
