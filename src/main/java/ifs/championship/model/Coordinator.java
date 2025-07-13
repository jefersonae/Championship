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

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String pass;

    private String email;

    @OneToOne
    @JoinColumn(name = "course_id", unique = true)
    private Course course;
}
