package ifs.championship.service;

import ifs.championship.model.*;
import ifs.championship.repository.AthleteRepository;
import ifs.championship.repository.CaptainRepository;
import ifs.championship.repository.CoordinatorRepository;
import ifs.championship.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinatorService {

    @Autowired
    private CoordinatorRepository coordinatorRepository;
    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private CaptainRepository captainRepository;

    public Captain createCaptain(Long coordinatorId, Long athleteId, Long sportId) {

        Coordinator coordinator = coordinatorRepository.findByEnrollment(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found with id: " + coordinatorId));
        if(coordinator.getCourse() == null){
           throw new IllegalStateException("Coordinator does not have a course assigned.");
       }
        Course courseCoordinator = coordinator.getCourse();

        Athlete athlete = athleteRepository.findByEnrollment(athleteId)
                .orElseThrow(() -> new IllegalArgumentException("Athlete not found with id: " + athleteId));

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found with id: " + sportId));

        Captain newCaptain = new Captain();
        newCaptain.setAthlete(athlete);
        newCaptain.setSport(sport);
        newCaptain.setCourse(courseCoordinator);

        try {
            return captainRepository.save(newCaptain);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("There is already a captain registered for the course "
                    + courseCoordinator.getName() + " in sport " + sport.getName() + ".");
        }
    }

    public List<Coordinator> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }

    public Coordinator getCoordinatorById(Long id) {
        return coordinatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found with id: " + id));
    }
}
