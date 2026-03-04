package it.spindox.stagelab.magazzino.services;
import java.math.BigDecimal;
import java.util.List;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentResponse;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionSearch;
import org.springframework.data.domain.Page;


public interface FatturaWorkExecutionService {

    // Gestisce il pagamento per una singola execution

    FatturaWorkExecutionPaymentResponse paymentCheckFattura(Long workExecutionId, BigDecimal pagatoDaAggiungere);

    // Esegue il ricalcolo per tutte le fatture collegate alle execution

    List<FatturaWorkExecutionPaymentResponse> paymentCheckAllFatture();

    int fixNullFields();

    Page<FatturaWorkExecutionPaymentResponse> search(FatturaWorkExecutionSearch req);
}