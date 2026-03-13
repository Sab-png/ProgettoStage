package it.spindox.stagelab.magazzino.services;
import java.math.BigDecimal;
import java.util.List;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoSearch;
import org.springframework.data.domain.Page;


public interface FatturaWorkExecutionService {

    // Gestisce il pagamento per una singola execution

    DtoPaymentResponse paymentCheckFattura(Long workExecutionId, BigDecimal pagatoDaAggiungere);

    // Esegue il ricalcolo per tutte le fatture collegate alle execution

    List<DtoPaymentResponse> paymentCheckAllFatture();
// CAMPI NULL

    int fixNullFields();
// SEARCH

    Page<DtoPaymentResponse> search(DtoSearch req);

}