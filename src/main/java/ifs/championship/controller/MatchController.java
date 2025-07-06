package ifs.championship.controller;

import ifs.championship.dto.ResultDTO;
import ifs.championship.model.Match;
import ifs.championship.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // Arbiter registers a result for a match
    @PostMapping("/{matchId}/result")
    public ResponseEntity<?> submitResult(
            @PathVariable Long matchId,
            @RequestBody ResultDTO resultDTO){
        try {
            Match match = matchService.registerResult(matchId, resultDTO);
            return ResponseEntity.ok(match);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Arbiter registers a walkover for a match
    @PostMapping("/{matchId}/undoWo")
    public ResponseEntity<?> undoWo (@PathVariable Long matchId) {
        try {
            Match match = matchService.undoWo(matchId);
            return ResponseEntity.ok(match);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}