package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "name")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String level; // "INTEGRADO", "TECNICO", "SUPERIOR"

    @OneToOne(mappedBy = "course") // 'mappedBy' aponta para o campo "curso" na entidade Coordenador
    @ToString.Exclude
    private Coordinator coordinator;
}