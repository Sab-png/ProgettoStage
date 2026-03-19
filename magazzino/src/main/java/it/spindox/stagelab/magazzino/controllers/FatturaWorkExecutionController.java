package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentRequest;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoSearch;
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
    public Page<DtoPaymentResponse> search(
            @ModelAttribute DtoSearch req
    ) {
        return fatturaWorkExecutionService.search(req);
    }



    // PATCH ID-payment

    @PatchMapping(
            path = "/{id}/payment",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<DtoPaymentResponse> paymentCheckFattura(
            @PathVariable Long id,
            @Valid @RequestBody DtoPaymentRequest request
    ) {
        if (request == null || request.getPagatoDaAggiungere() == null) {
            return ResponseEntity.badRequest().build();
        }

        DtoPaymentResponse updated =
                fatturaWorkExecutionService.paymentCheckFattura(
                        id,
                        request.getPagatoDaAggiungere()
                );

        return ResponseEntity.ok(updated);
    }




    // POST /payment-check-all

    @PostMapping("/payment-check-all")
    public ResponseEntity<Map<String, Object>> checkAllFatture() {

        List<DtoPaymentResponse> items =
                fatturaWorkExecutionService.paymentCheckAllFatture();

        Map<String, Object> body = Map.of(
                "updatedCount", fatturaWorkExecutionService.getLastUpdatedCount(),
                "items", items
        );             // lista dei problemi


        return ResponseEntity.ok(body);
    }
}