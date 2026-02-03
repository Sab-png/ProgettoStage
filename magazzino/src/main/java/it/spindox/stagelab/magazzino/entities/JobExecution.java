package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "JOB_EXECUTION")
public class JobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_exec_seq_gen")
    @SequenceGenerator(
            name = "job_exec_seq_gen",
            sequenceName = "JOB_EXECUTION_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusJob status;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(length = 2000)
    private String errorMessage;
}

