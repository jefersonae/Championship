package ifs.championship.repository;

import ifs.championship.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    // "SELECT * FROM atleta WHERE matricula = ?"
    Optional<Athlete> findByEnrollment (Long enrollment);

    // "SELECT * FROM atleta WHERE matricula IN (?, ?, ...)"
    List<Athlete> findAllByEnrollmentIn(List<Long> enrollments);
}