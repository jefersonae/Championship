package ifs.championship.controller;

import ifs.championship.dto.AthleteRegisterDTO;
import ifs.championship.dto.TeamRegisterDTO;
import ifs.championship.model.Athlete;
import ifs.championship.model.Team;
import ifs.championship.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/athletes")
public class AthleteController {

    @Autowired
    private AthleteService athleteService;

    @PostMapping
    public ResponseEntity<?> createAthlete(@RequestBody AthleteRegisterDTO athleteDTO) {
        try {
            Athlete athleteRegistered = athleteService.createAthlete(athleteDTO);
            return ResponseEntity.status(201).body(athleteRegistered);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAthletes() {
        return ResponseEntity.ok(athleteService.getAllAthletes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAthleteById(@PathVariable Long id) {
        Athlete athlete = athleteService.getAthleteById(id);
        if (athlete != null) {
            return ResponseEntity.ok(athlete);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}