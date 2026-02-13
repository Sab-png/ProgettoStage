package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Slf4j
@Component
public class JobExecutionMapperImpl implements JobExecutionMapper {


     // Converte un nome job + stato in una entity JobExecution “nuova”.

     // Viene usato in:
     // create() / start() di un job

     // startTime viene impostata al momento della creazione
     // L'ID è generato dal DB

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        JobExecution job = new JobExecution();
        job.setStatus(status);

        // startTime: impostata ora

        job.setStartTime(LocalDateTime.now());

        return job;
    }


      // Converte una entity JobExecution in DTO JobExecutionResponse.

     // Viene usato quando si espone lo stato di esecuzione verso il client:

     // GET /jobs/{id}
      // GET /jobs/list
       // POST /jobs/search

      // Protegge da null su entity

     // startTime / endTime potrebbero essere null (job non terminato)
     // Se i campi temporeali sono già LocalDateTime, NON serve .toLocalDateTime()



    @Override
    public JobExecutionResponse toResponse(JobExecution entity) {
        if (entity == null) return null;

        return JobExecutionResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())

                // Se entity.getStartTime() è già LocalDateTime, si  usa:

                .startTime(entity.getStartTime().toLocalDateTime() /* != null ? entity.getStartTime() : null */)
                .endTime(entity.getEndTime().toLocalDateTime() /* != null ? entity.getEndTime() : null */)
                .errorType(entity.getErrorType())
                .errorMessage(entity.getErrorMessage())
                .build();
    }


      // Aggiorna lo stato e chiude il job impostando endTime ora.

     // Viene usato in:
     // complete() / fail() / cancel()

      // endTime viene impostato ad ora/adesso
     // errorMessage opzional (utile in caso di errori)


    @Override
    public void updateEntity(JobExecution target, StatusJob status, String errorMessage) {
        target.setStatus(status);
        target.setEndTime(LocalDateTime.now());
        target.setErrorMessage(errorMessage);
    }


     // Aggiorna lo stato, chiude il job, e valorizza l’errore tipizzato.
     // Viene usato in:
      // fail() / error()
      // endTime viene impostato a now
      // errorType + errorMessage forniscono dettagli diagnostici


    @Override
    public void updateEntity(JobExecution target, StatusJob status, StatusJobErrorType errorType, String errorMessage) {
        target.setStatus(status);
        target.setEndTime(LocalDateTime.now());
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);
    }
}
