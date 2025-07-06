package ifs.championship.controller;

import ifs.championship.dto.TeamRegisterDTO;
import ifs.championship.model.Team;
import ifs.championship.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createTeam( TeamRegisterDTO teamDTO) {
        try {
            Team newTeam = teamService.createTeam(
                    teamDTO.getTeamName(),
                    teamDTO.getCourseId(),
                    teamDTO.getSportId(),
                    teamDTO.getCaptainId(),
                    teamDTO.getAthleteEnrollment()
            );
            return ResponseEntity.status(201).body(newTeam);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}