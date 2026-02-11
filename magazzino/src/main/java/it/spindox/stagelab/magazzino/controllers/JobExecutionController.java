package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.mappers.JobExecutionMapper;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final JobExecutionMapper jobExecutionMapper;   // ① Iniettato

    @GetMapping("/{id}")
    public ResponseEntity<JobExecutionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobExecutionService.getById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<JobExecutionResponse>> search(
            @Valid @RequestBody JobExecutionRequest request
    ) {
        return ResponseEntity.ok(jobExecutionService.search(request));
    }

    @GetMapping("/errors/last")
    public ResponseEntity<JobExecutionResponse> getLastError() {

        Optional<JobExecution> last = jobExecutionService.findLast();

        if (last.isEmpty() || last.get().getErrorMessage() == null) {
            return ResponseEntity.noContent().build();
        }

        //  si usa il mapper iniettato
        return ResponseEntity.ok(jobExecutionMapper.toResponse(last.get()));
    }
}