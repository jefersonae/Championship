package ifs.championship.service;

import ifs.championship.dto.ClassificationDTO;
import ifs.championship.model.Group;
import ifs.championship.model.Inscription;
import ifs.championship.model.Match;
import ifs.championship.model.Team;

import java.util.*;
import java.util.stream.Collectors;

public class ClassificationService {

    public List<ClassificationDTO> calculateClassification(Group group) {
        Map<Long, ClassificationDTO> classificationMap = new HashMap<>();
        List<Team> groupTeams = group.getInscriptions().stream()
                .map(Inscription::getTeam)
                .toList();
        for (Team team : groupTeams) {
            classificationMap.put(team.getId(), new ClassificationDTO(team,
                    0, 0, 0, 0, 0, 0, 0));
        }

        // Itera sobre os jogos do grupo para calcular os pontos
        for (Match match : group.getMatches()) {
            if (match.getStatus() != null && match.getStatus().matches("FINALIZADO|WO")) {
                updateStatics(match, classificationMap);
            }
        }

        List<ClassificationDTO> classificationFinal = new ArrayList<>(classificationMap.values());
        classificationFinal.sort(Comparator.comparing(ClassificationDTO::getPoints)
                .thenComparing(ClassificationDTO::getGoalDifference)
                .thenComparing(ClassificationDTO::getGoalsFor)
                .reversed());

        return classificationFinal;
    }

    private void updateStatics(Match match, Map<Long, ClassificationDTO> classificationMap) {
        ClassificationDTO statsA = classificationMap.get(match.getTeamA().getId());
        ClassificationDTO statsB = classificationMap.get(match.getTeamB().getId());

        int pointsA = match.getTeamAScore();
        int pointsB = match.getTeamBScore();

        statsA.setGoalsFor(statsA.getGoalsFor() + pointsA);
        statsA.setGoalsAgainst(statsA.getGoalsAgainst() + pointsB);
        statsB.setGoalsFor(statsB.getGoalsFor() + pointsB);
        statsB.setGoalsAgainst(statsB.getGoalsAgainst() + pointsA);

        statsA.setGoalDifference(statsA.getGoalsFor() - statsA.getGoalsAgainst());
        statsB.setGoalDifference(statsB.getGoalsFor() - statsB.getGoalsAgainst());

        if(pointsA > pointsB) { // Vitoria equipe A
            statsA.setPoints(statsA.getPoints() + 3);
            statsA.setWins(statsA.getWins() + 1);
            statsB.setLosses(statsB.getLosses() + 1);
        } else if(pointsA < pointsB) { // Vitoria equipe B
            statsB.setPoints(statsB.getPoints() + 3);
            statsB.setWins(statsB.getWins() + 1);
            statsA.setLosses(statsA.getLosses() + 1);
        } else { // Empate
            statsA.setPoints(statsA.getPoints() + 1);
            statsB.setPoints(statsB.getPoints() + 1);
            statsA.setDraws(statsA.getDraws() + 1);
            statsB.setDraws(statsB.getDraws() + 1);
        }
    }
}
