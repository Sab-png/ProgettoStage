package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.mappers.JobExecutionMapper;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;



@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Validated
public class JobExecutionController {

    private final JobExecutionService jobExecutionService;
    private final JobExecutionMapper jobExecutionMapper;


      // GET /jobs
     // Restituisce SOLO gli ID dei job filtrati (Page<Long>)

    @GetMapping
    public ResponseEntity<Page<Long>> getJobIds(
            @RequestParam(required = false) String nomeJob,
            @RequestParam(required = false) String stato,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        JobExecutionRequest req = new JobExecutionRequest();
        req.setNomeJob(nomeJob);
        req.setStato(stato);
        req.setFrom(from);
        req.setTo(to);
        req.setPage(page);
        req.setSize(size);

        Page<Long> ids = jobExecutionService.searchIds(req);
        return ResponseEntity.ok(ids);
    }


     // GET BY ID: /jobs/{id}


    @GetMapping("/{id}")
    public ResponseEntity<JobExecutionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobExecutionService.getById(id));
    }
// GET LIST JOB

    @GetMapping("/list")
    public ResponseEntity<Page<JobExecutionResponse>> getAllFatturePaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        return ResponseEntity.ok(jobExecutionService.getAllPaged(page, size));
    }

      // Ricerca completa/search con DTO

    @PostMapping("/search")
    public ResponseEntity<Page<JobExecutionResponse>> search(
            @Valid @RequestBody JobExecutionRequest request
    ) {
        return ResponseEntity.ok(jobExecutionService.search(request));
    }


     // GET /jobs/errors/last
      // Ritorna l'ultimo job che ha generato un errore

    @GetMapping("/errors/last")
    public ResponseEntity<JobExecutionResponse> getLastError() {

        Optional<JobExecution> last = jobExecutionService.findLast();

        if (last.isEmpty() || last.get().getErrorMessage() == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(jobExecutionMapper.toResponse(last.get()));
    }
}