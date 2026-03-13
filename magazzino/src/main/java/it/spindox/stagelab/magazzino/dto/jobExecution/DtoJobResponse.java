package it.spindox.stagelab.magazzino.dto.jobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
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

public class DtoJobResponse {

    private Long id;
    private StatusJob status;
    private LocalDateTime startTime;   //  LocalDateTime
    private LocalDateTime endTime;     //  LocalDateTime
    private StatusJobErrorType errorType;
    private String errorMessage;
}