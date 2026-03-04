package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.OffsetDateTime;



@Slf4j
@Entity
@Table(name = "FATTURA_WORK_EXECUTION")
@Data

public class FatturaWorkExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fatturajob_exec_seq")
    @SequenceGenerator(
           name = "fatturajob_exec_seq",
           sequenceName = "FATTURA_WORK_EXECUTION_SEQ",
           allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "FATTURA_ID", nullable = false)
    private Long fatturaId;
    @Enumerated(EnumType.STRING)


    @Column(name = "START_TIME", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "END_TIME")
    private OffsetDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ERROR_TYPE")
    private StatusJobErrorType errorType;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;

    @Column(name = "STATUS", nullable = false)
    private StatusJob status;
}