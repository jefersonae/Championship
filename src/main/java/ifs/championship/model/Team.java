package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
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

    private String name;

    @ManyToOne
    @JoinColumn(name= "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @OneToOne
    @JoinColumn(name = "captain_id")
    private Athlete technical;

    @ManyToMany
    @JoinTable(
            name = "team_athlete",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private List<Athlete> athletes;

    @OneToMany(mappedBy = "teamA")
    private List<Match> matchesWithTeamA;

    @OneToMany(mappedBy = "teamB")
    private List<Match> matchesWithTeamB;
}