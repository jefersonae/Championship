package ifs.championship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
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
    @JsonBackReference
    private Course course;
}
