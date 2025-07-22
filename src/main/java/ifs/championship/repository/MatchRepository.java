package ifs.championship.repository;

import ifs.championship.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByPhase(String phase);
    List<Match> findByGroupId(Long groupId);
}
