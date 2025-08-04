package ifs.championship.controller;

import ifs.championship.dto.TeamRegisterDTO;
import ifs.championship.model.Team;
import ifs.championship.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamRegisterDTO teamDTO) {
        if (teamDTO.getCourseId() == null || teamDTO.getSportId() == null || teamDTO.getCaptainId() == null) {
            return ResponseEntity.badRequest().body("IDs não podem ser nulos");
        }
        return ResponseEntity.ok(teamService.createTeam(teamDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        if (team != null) {
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}