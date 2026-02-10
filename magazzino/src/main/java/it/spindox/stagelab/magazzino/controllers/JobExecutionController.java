package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Validated
public class JobExecutionController {

    private final JobExecutionService jobExecutionService;

    /**
     * Recupera una JobExecution per ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobExecutionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobExecutionService.getById(id));
    }

    /**
     * Ricerca JobExecution con filtri (POST)
     */
    @PostMapping("/search")
    public ResponseEntity search(
            @Valid @RequestBody JobExecutionRequest request
    ) {
        return ResponseEntity.ok(jobExecutionService.search(request));
    }

    /**
     * Ultimo job fallito
     */
    @GetMapping("/errors/last")
    public ResponseEntity<JobExecutionResponse> getLastError() {

        Optional<JobExecution> last = jobExecutionService.findLast();

        if (last.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        JobExecution job = last.get();

        JobExecutionResponse response = new JobExecutionResponse(
                job.getId(),
                job.getStatus(),
                job.getStartTime().toLocalDateTime(),
                job.getEndTime() != null ? job.getEndTime().toLocalDateTime() : null,
                job.getErrorType(),
                job.getErrorMessage()
        );

        return ResponseEntity.ok(response);
    }
}

