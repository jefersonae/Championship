package ifs.championship.model;

import ifs.championship.model.enums.CourseLevel;
import ifs.championship.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<Group> groups;
}
