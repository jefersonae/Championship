package ifs.championship.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String nickname;

    @Column(unique = true, nullable = false)
    private int enrollment;

    private String phone;

    @Column(nullable = false)
    private String pass;
}
