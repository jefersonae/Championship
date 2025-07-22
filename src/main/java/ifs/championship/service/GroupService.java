package ifs.championship.service;

import ifs.championship.model.*;
import ifs.championship.repository.EventRepository;
import ifs.championship.repository.GroupRepository;
import ifs.championship.repository.InscriptionRepository;
import ifs.championship.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Group> generateGroupsForEvent(Long eventId) {

        Event event= eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<Inscription> inscriptions = event.getInscriptions();
        int totalTeams = inscriptions.size();

        if(totalTeams < 3) {
            throw new IllegalArgumentException("Not enough inscriptions to create groups");
        }
        Collections.shuffle(inscriptions);

        List<Integer> groupSizes = calculateGroupSizes(totalTeams);

        List<Group> groups = new ArrayList<>();
        int startIndex = 0;
        char groupName = 'A';

        for (Integer size : groupSizes) {
            Group newGroup = new Group();
            newGroup.setName("Grupos " + groupName++);
            newGroup.setEvent(event);

            List<Inscription> groupInscriptions = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Inscription inscription = inscriptions.get(startIndex++);
                inscription.setGroup(newGroup);
                inscriptionRepository.save(inscription);
                groupInscriptions.add(inscription);
            }

            newGroup.setInscriptions(groupInscriptions);
            groupRepository.save(newGroup);

            groups.add(newGroup);
        }
        return groups;
    }

    public void generateMatchesForGroups(Group group, List<Inscription> groupInscriptions) {
       List<Team> teams = new ArrayList<>();
       for (Inscription inscription : groupInscriptions) {
           teams.add(inscription.getTeam());
       }
       for (int i = 0; i < teams.size(); i++) {
           for (int j = i + 1; j < teams.size(); j++) {
               Match newMatch = new Match();
               newMatch.setGroup(group);
               newMatch.setTeamA(teams.get(i));
               newMatch.setTeamB(teams.get(j));
               newMatch.setPhase("GRUPO");
               newMatch.setStatus("AGENDADO");
               matchRepository.save(newMatch);
           }
       }
    }

    private List<Integer> calculateGroupSizes(int totalTeams) {
        List<Integer> sizes = new ArrayList<>();

        switch (totalTeams){
            case 3: sizes.add(3); break;
            case 4: sizes.add(4); break;
            case 5: sizes.add(5); break;
            case 6: sizes.add(3); sizes.add(3); break;
            case 7: sizes.add(3); sizes.add(4); break;
            case 8: sizes.add(4); sizes.add(4); break;
            case 9: sizes.add(3); sizes.add(3); sizes.add(3); break;
            case 10: sizes.add(3); sizes.add(3); sizes.add(4); break;
            case 11: sizes.add(3); sizes.add(4); sizes.add(4); break;
            case 12: sizes.add(3); sizes.add(3); sizes.add(3); sizes.add(3); break;
            default:
                int qntTeams = totalTeams;
                while (qntTeams > 0) {
                    if (qntTeams % 3 == 0) {
                        sizes.add(3); qntTeams -= 3;
                    } else if (qntTeams % 4 == 0) {
                        sizes.add(4); qntTeams -= 4;
                    } else if (qntTeams >= 5) {
                        sizes.add(5); qntTeams -= 5;
                    } else {
                        throw new IllegalStateException("Não é possível dividir " + totalTeams + " equipes em grupos de 3, 4 ou 5.");
                    }
                }
                break;
        }
        return sizes;
    }
}
