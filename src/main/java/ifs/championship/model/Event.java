package ifs.championship.model;

import ifs.championship.model.enums.CourseLevel;
import ifs.championship.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseLevel courseLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private List<Group> groups;
}
