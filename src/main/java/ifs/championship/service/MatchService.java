package ifs.championship.service;

import ifs.championship.dto.ResultDTO;
import ifs.championship.model.Match;
import ifs.championship.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public Match registerResult(Long matchId, ResultDTO resultDTO){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (resultDTO.isWo()) {
            match.setStatus("WO");
            if(resultDTO.getWinner() == 'A'){
                match.setTeamAScore(1);
                match.setTeamBScore(0);
            } else if(resultDTO.getWinner() == 'B'){
                match.setTeamAScore(0);
                match.setTeamBScore(1);
            } else {
                throw new RuntimeException("Invalid winner specified for WO match");
            }
        } else {
            match.setTeamAScore(resultDTO.getScoreTeam1());
            match.setTeamBScore(resultDTO.getScoreTeam2());
            match.setStatus("FINALIZADO");
        }

        return matchRepository.save(match);
    }

    public Match undoWo(Long matchId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (!"WO".equals(match.getStatus())) {
            throw new RuntimeException("Match is not in WO status");
        }

        match.setStatus("AGENDADO");
        match.setTeamAScore(null);
        match.setTeamBScore(null);

        return matchRepository.save(match);
    }
}
