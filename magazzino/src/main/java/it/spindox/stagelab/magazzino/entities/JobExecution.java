package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


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
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;
}