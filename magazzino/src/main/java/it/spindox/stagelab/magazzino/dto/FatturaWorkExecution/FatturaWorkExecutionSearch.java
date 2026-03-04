package it.spindox.stagelab.magazzino.dto.FatturaWorkExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;


@Data
public class FatturaWorkExecutionSearch {

    private StatusJob status;

    // Filtro per risultati che hanno o non hanno errori
    private Boolean hasError;

    private Long fatturaId;

    // Intervallo temporale della startTime del job

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTo;

    // Paging
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}