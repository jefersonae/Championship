package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollment;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String pass;

    @OneToOne
    @JoinColumn(name = "course_id", unique = true)
    private Course course;
}
