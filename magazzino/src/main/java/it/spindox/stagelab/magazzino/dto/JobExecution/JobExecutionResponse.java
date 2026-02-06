package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobExecutionResponse {

    private Long id;
    private StatusJob status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String errorMessage;

    public void setStatus(it.spindox.stagelab.magazzino.entities.StatusJob status) {
    }
}
