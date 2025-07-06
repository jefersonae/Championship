package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "sport_id"})
})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name= "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @OneToOne
    @JoinColumn(name = "captain_id", nullable = false)
    private Athlete technical;

    @ManyToMany
    @JoinTable(
            name = "team_athlete",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private List<Athlete> athletes;

    @OneToMany(mappedBy = "teamA")
    @ToString.Exclude
    private List<Match> matchesWithTeamA;

    @OneToMany(mappedBy = "teamB")
    @ToString.Exclude
    private List<Match> matchesWithTeamB;
}