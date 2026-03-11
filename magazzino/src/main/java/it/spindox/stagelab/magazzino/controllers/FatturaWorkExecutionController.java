package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentRequest;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentResponse;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionSearch;
import it.spindox.stagelab.magazzino.services.FatturaWorkExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;




@RestController
@RequestMapping("/fattureworkexecution")
@RequiredArgsConstructor
@Validated

public class FatturaWorkExecutionController {

    private final FatturaWorkExecutionService fatturaWorkExecutionService;


    // GET /fattureworkexecution/search

    @GetMapping("/search")
    public Page<FatturaWorkExecutionPaymentResponse> search(
            @ModelAttribute FatturaWorkExecutionSearch req
    ) {
        return fatturaWorkExecutionService.search(req);
    }



    // PATCH ID-payment

    @PatchMapping(
            path = "/{id}/payment",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<FatturaWorkExecutionPaymentResponse> paymentCheckFattura(
            @PathVariable Long id,
            @Valid @RequestBody FatturaWorkExecutionPaymentRequest request
    ) {
        if (request == null || request.getPagatoDaAggiungere() == null) {
            return ResponseEntity.badRequest().build();
        }

        FatturaWorkExecutionPaymentResponse updated =
                fatturaWorkExecutionService.paymentCheckFattura(
                        id,
                        request.getPagatoDaAggiungere()
                );

        return ResponseEntity.ok(updated);
    }



    // POST /payment-check-all

    @PostMapping("/payment-check-all")
    public ResponseEntity<Map<String, Object>> checkAllFatture() {

        List<FatturaWorkExecutionPaymentResponse> items =
                fatturaWorkExecutionService.paymentCheckAllFatture();

        Map<String, Object> body = Map.of(
                "updatedCount", items.size(),
                "items", items
        );

        return ResponseEntity.ok(body);
    }
}