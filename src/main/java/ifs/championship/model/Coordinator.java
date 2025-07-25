package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "enrollment")
@Entity
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollment;

    private String name;

    @Column(unique = true)
    private String pass;

    private String email;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
