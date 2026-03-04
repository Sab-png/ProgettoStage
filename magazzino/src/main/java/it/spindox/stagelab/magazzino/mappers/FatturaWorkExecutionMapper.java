package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentResponse;
import it.spindox.stagelab.magazzino.entities.*;


public interface FatturaWorkExecutionMapper {

    // Crea una nuova execution legata a una fattura

    FatturaWorkExecution toEntity(String workName, StatusJob status, Fattura fattura);

    // Costruisce la response

    FatturaWorkExecutionPaymentResponse toPaymentResponse(Fattura fattura, FatturaWorkExecution exec);

    // Aggiorna stato e messaggi job

    void updateEntity(FatturaWorkExecution target, StatusJob status, String errorMessage);

    // Aggiorna stato + tipo errore job

    void updateEntity(FatturaWorkExecution target,
                      StatusJob status,
                      StatusJobErrorType errorType,
                      String errorMessage);
}