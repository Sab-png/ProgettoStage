package it.spindox.stagelab.magazzino.dto.FatturaWorkExecution;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecution;



  // Filtro di ricerca per WorkExecution

@Data
public class FatturaWorkExecutionSearch {

    // Stato della JOB EXECUTION

    private SXFatturaJobexecution status;

    // Filtro per risultati che hanno o non hanno errori

    private Boolean hasError;

    // Filtra per id fattura

    private Long fatturaId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTo;

    // PagE

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}