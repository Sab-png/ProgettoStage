package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.entities.*;




public interface FatturaWorkExecutionMapper {

    // Crea una nuova execution legata a una fattura

    FatturaWorkExecution toEntity(String workName, SXFatturaJobexecution status, Fattura fattura);

    FatturaWorkExecution toEntity(String workName, StatusJob status, Fattura fattura);

    // Costruisce la response

    DtoPaymentResponse toPaymentResponse(Fattura fattura, FatturaWorkExecution exec);

    // Aggiorna stato e messaggio di errore (senza errorType)

    void updateEntity(FatturaWorkExecution target,
                      SXFatturaJobexecution status,
                      String errorMessage);

    // Aggiorna stato + tipo errore job (scheduler)

    void updateEntity(FatturaWorkExecution target,
                      SXFatturaJobexecution status,
                      StatusJobErrorType errorType,
                      String errorMessage);

    // Aggiorna stato + tipo errore fattura

    void updateEntity(FatturaWorkExecution target,
                      SXFatturaJobexecution status,
                      SXFatturaJobexecutionErrorType errorType,
                      String errorMessage);
}