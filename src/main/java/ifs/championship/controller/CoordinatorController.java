package ifs.championship.controller;

import ifs.championship.dto.CaptainDTO;
import ifs.championship.model.Captain;
import ifs.championship.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {
    @Autowired
    private CoordinatorService coordinatorService;

    @PostMapping("/{enrollmentId}/createCaptain")
    public ResponseEntity<?> createCaptain(
            @PathVariable Long enrollmentId,
            @RequestBody CaptainDTO captainDTO) {
        try {
            Captain newCaptain = coordinatorService.createCaptain(
                    enrollmentId,
                    captainDTO.getEnrollmentId(),
                    captainDTO.getSportId()
            );
            return ResponseEntity.status(201).body(newCaptain);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCoordinators() {
        return ResponseEntity.ok(coordinatorService.getAllCoordinators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoordinatorById(@PathVariable Long id) {
        var coordinator = coordinatorService.getCoordinatorById(id);
        if (coordinator != null) {
            return ResponseEntity.ok(coordinator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}