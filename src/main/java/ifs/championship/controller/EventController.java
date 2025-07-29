package ifs.championship.controller;

import ifs.championship.dto.EventDTO;
import ifs.championship.model.Event;
import ifs.championship.model.Group;
import ifs.championship.model.Match;
import ifs.championship.service.EventService;
import ifs.championship.service.GroupService;
import ifs.championship.service.KnockoutPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private GroupService groupService;

    private KnockoutPhaseService knockoutPhaseService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        Event createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @PostMapping("/{eventId}/generate-groups")
    public ResponseEntity<?> generateGroups(@PathVariable Long eventId) {
        try {
            List<Group> groups = groupService.generateGroupsForEvent(eventId);
            return ResponseEntity.ok(groups);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{eventId}/knockoutPhase")
    public ResponseEntity<?> knockoutPhase(@PathVariable Long eventId) {
        try {
            List<Match> matches = knockoutPhaseService.createKnockoutPhase(eventId);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
