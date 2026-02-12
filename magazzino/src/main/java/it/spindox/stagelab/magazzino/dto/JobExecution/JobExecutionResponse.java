package it.spindox.stagelab.magazzino.dto.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionResponse {

    private Long id;
    private StatusJob status;
    private LocalDateTime startTime;   //  LocalDateTime
    private LocalDateTime endTime;     //  LocalDateTime
    private SJobErrorType errorType;
    private String errorMessage;
}