package ifs.championship.repository;

import ifs.championship.model.Athlete;
import ifs.championship.model.Captain;
import ifs.championship.model.Course;
import ifs.championship.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptainRepository extends JpaRepository<Captain, Long> {
    boolean existsByAthleteAndCourseAndSport(Athlete athlete, Course course, Sport sport);
}
