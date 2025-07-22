package ifs.championship.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ifs.championship.model.enums.CourseLevel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(of = "name")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CourseLevel level; // "INTEGRADO", "TECNICO", "SUPERIOR"

    @OneToOne(mappedBy = "course") // 'mappedBy' aponta para o campo "curso" na entidade Coordenador
    private Coordinator coordinator;
}