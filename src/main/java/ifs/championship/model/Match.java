package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Optional;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String phase; // Ex: "Fase de Grupos", "Quartas de Final", "Semifinal", "Final"

    @ManyToOne
    @JoinColumn(name="teamA_id")
    private Team teamA;

    @ManyToOne
    @JoinColumn(name="teamB_id")
    private Team teamB;

    private Integer teamAScore;
    private Integer teamBScore;

    private LocalDate matchDate;

    private String status; // Ex: "AGENDADO", "FINALIZADO", "WO"

    @Transient
    public Optional<Team> getWinner() {
        // 1. Se o jogo não terminou, não há vencedor
        if (this.status == null || !this.status.matches("FINALIZADO|WO")) {
            return Optional.empty();
        }

        // 2. Se os placares não foram definidos, não é possível determinar
        if (this.teamAScore == null || this.teamBScore == null) {
            return Optional.empty();
        }

        // 3. Se foi empate, não há um único vencedor
        if (this.teamAScore.equals(this.teamBScore)) {
            return Optional.empty();
        }

        // 4. Compara os placares e retorna a equipe vencedora
        return this.teamAScore > this.teamBScore ? Optional.of(this.teamA) : Optional.of(this.teamB);
    }
}