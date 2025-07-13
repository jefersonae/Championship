package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @Column(nullable = false)
    private String phase; // Ex: "Fase de Grupos", "Quartas de Final", "Semifinal", "Final"

    @ManyToOne
    @JoinColumn(name="teamA_id", nullable = false)
    private Team teamA;

    @ManyToOne
    @JoinColumn(name="teamB_id", nullable = false)
    private Team teamB;

    private Integer teamAScore;
    private Integer teamBScore;

    private LocalDate matchDate;

    private String status; // Ex: "AGENDADO", "FINALIZADO", "WO"
}