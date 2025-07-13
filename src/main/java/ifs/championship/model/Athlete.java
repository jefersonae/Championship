package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "enrollment")
@Entity
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long enrollment;

    @Column(nullable = false)
    private String fullName;

    private String nickname;

    private String phone;

    @Column(nullable = false)
    private String pass;
}
