package ifs.championship.service;

import ifs.championship.dto.ClassificationDTO;
import ifs.championship.model.*;
import ifs.championship.repository.EventRepository;
import ifs.championship.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class KnockoutPhaseService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Match> createKnockoutPhase(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        // Validação e coleta de classificados
        for (Group group : event.getGroups()) {
            for (Match match : group.getMatches()) {
                if (match.getStatus() == null || !match.getStatus().matches("FINALIZADO|WO")) {
                    throw new IllegalStateException("Match " + match.getId() + " in group " + group.getId() + " has no winner.");
                }
            }
        }
        List<ClassificationDTO> firstPlaces = new ArrayList<>();
        List<ClassificationDTO> secondPlaces = new ArrayList<>();
        for (Group group : event.getGroups()) {
            List<ClassificationDTO> classification = classificationService.calculateClassification(group);
            if (classification.size() < 2) {
                throw new IllegalStateException("Not enough teams in group " + group.getId() + " to create knockout phase.");
            }
            firstPlaces.add(classification.get(0));
            secondPlaces.add(classification.get(1));
        }

        // Lógica de "byes"
        int totalTeams = firstPlaces.size() + secondPlaces.size();
        int sizeKey = nextPowerOfTwo(totalTeams);
        int numberByes = sizeKey - totalTeams;
        firstPlaces.sort(Comparator.comparing(ClassificationDTO::getPoints)
                .thenComparing(ClassificationDTO::getGoalDifference)
                .thenComparing(ClassificationDTO::getGoalsFor)
                .reversed());
        List<Team> teamByes = new ArrayList<>();
        for (int i = 0; i < numberByes; i++) {
            teamByes.add(firstPlaces.get(i).getTeam());
        }

        // Sorteio dos potes
        List<Team> pote1 = new ArrayList<>(
                firstPlaces.stream().skip(numberByes).map(ClassificationDTO::getTeam).toList()
        );
        List<Team> pote2 = new ArrayList<>(
                secondPlaces.stream().map(ClassificationDTO::getTeam).toList()
        );
        Collections.shuffle(pote1);
        Collections.shuffle(pote2);

        String phase = getPhaseName(sizeKey);

        List<Match> knockoutMatches = new ArrayList<>();
        // Monta os jogos pegando um time de cada pote
        for (int i = 0; i < pote1.size(); i++) {
            Match match = new Match();
            match.setTeamA(pote1.get(i));
            match.setTeamB(pote2.get(i));
            match.setPhase(phase);
            match.setStatus("AGENDADO");
            match.setGroup(null);

            knockoutMatches.add(matchRepository.save(match));
        }
        return knockoutMatches;
    }

    private int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

    private String getPhaseName(int bracketSize) {
        switch (bracketSize) {
            case 16:
                return "OITAVAS_DE_FINAL";
            case 8:
                return "QUARTAS_DE_FINAL";
            case 4:
                return "SEMIFINAL";
            case 2:
                return "FINAL";
            default:
                // Fallback caso o número seja diferente (ex: 32)
                return "ELIMINATORIA_RODADA_DE_" + bracketSize;
        }
    }
}