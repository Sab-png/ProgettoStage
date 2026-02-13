package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "JOB_EXECUTION")
@Data
public class JobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_exec_seq")
    @SequenceGenerator(
            name = "job_exec_seq",
            sequenceName = "JOB_EXECUTION_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusJob status;

    @Column(name = "START_TIME", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "END_TIME")
    private OffsetDateTime endTime;


    @Enumerated(EnumType.STRING)
    @Column(name = "ERROR_TYPE")
    private StatusJobErrorType errorType;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;

    public void setStartTime(OffsetDateTime from) {
        this.startTime = from;
    }

    public void setStartTime(LocalDateTime now) {
        this.startTime = now.atOffset(ZoneOffset.UTC);
    }

    public void setEndTime(LocalDateTime now) {
        this.endTime = now.atOffset(ZoneOffset.UTC);
    }
}