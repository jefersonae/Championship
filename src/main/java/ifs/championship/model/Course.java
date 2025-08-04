package ifs.championship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ifs.championship.model.enums.CourseLevel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CourseLevel level; // "INTEGRADO", "TECNICO", "SUPERIOR"

    @OneToOne(mappedBy = "course", fetch = FetchType.LAZY) // 'mappedBy' aponta para o campo "curso" na entidade Coordenador
    @JsonManagedReference
    private Coordinator coordinator;
}