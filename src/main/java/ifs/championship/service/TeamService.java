package ifs.championship.service;

import ifs.championship.model.Athlete;
import ifs.championship.model.Course;
import ifs.championship.model.Sport;
import ifs.championship.model.Team;
import ifs.championship.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private CaptainRepository captainRepository;

    public Team createTeam(String teamName, Long courseId, Long sportId, Long captainId, List<Long> athleteEnrollment) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found with id: " + sportId));

        Athlete captain = athleteRepository.findById(captainId)
                .orElseThrow(() -> new IllegalArgumentException("Captain not found with id: " + captainId));

        boolean isCaptain = captainRepository.existsByAthleteAndCourseAndSport(captain, course, sport);

        if(!isCaptain) {
            throw new IllegalStateException("The specified captain is not registered for the course and sport.");
        }

        List<Athlete> athletes = athleteRepository.findAllById(athleteEnrollment);
        if(athletes.size() != athleteEnrollment.size()) {
            throw new IllegalArgumentException("One or more athletes not found with the provided IDs.");
        }

        if(athletes.size() < sport.getMinAthletes() || athletes.size() > sport.getMaxAthletes()) {
            throw new IllegalArgumentException("Number of athletes must be between " + sport.getMinAthletes() + " and " + sport.getMaxAthletes());
        }

        Team newTeam = new Team();
        newTeam.setName(teamName);
        newTeam.setCourse(course);
        newTeam.setSport(sport);
        newTeam.setTechnical(captain);
        newTeam.setAthletes(athletes);

        return teamRepository.save(newTeam);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + id));
    }
}
