package ifs.championship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ifs.championship.model.enums.CourseLevel;
import ifs.championship.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
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

    @JsonBackReference
    @OneToMany(mappedBy = "event",fetch = FetchType.LAZY)
    private List<Inscription> inscriptions;

    @JsonBackReference
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Group> groups;
}
