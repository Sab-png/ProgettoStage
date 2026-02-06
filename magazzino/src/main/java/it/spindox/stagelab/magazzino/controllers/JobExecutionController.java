package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}