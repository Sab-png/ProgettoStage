package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
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
    private SJobErrorType errorType;
    private String errorMessage;

    public JobExecutionResponse(Long id,
                                StatusJob status,
                                LocalDateTime startTime,
                                LocalDateTime endTime,
                                SJobErrorType errorType,
                                String errorMessage) {
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public JobExecutionResponse() {}
}

