package ifs.championship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String phase; // Ex: "Fase de Grupos", "Quartas de Final", "Semifinal", "Final"

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="teamA_id")
    private Team teamA;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="teamB_id")
    private Team teamB;

    private Integer teamAScore;
    private Integer teamBScore;

    private LocalDate matchDate;

    private String status; // Ex: "AGENDADO", "FINALIZADO", "WO"
}