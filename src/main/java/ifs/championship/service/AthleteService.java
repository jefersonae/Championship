package ifs.championship.service;

import ifs.championship.dto.AthleteRegisterDTO;
import ifs.championship.model.Athlete;
import ifs.championship.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AthleteService {
    @Autowired
    private AthleteRepository athleteRepository;

    public Athlete createAthlete(AthleteRegisterDTO athleteDTO) {

        if (athleteRepository.findById(athleteDTO.getEnrollment()).isPresent()) {
            throw new IllegalArgumentException("Athlete with this enrollment already exists");
        }

        Athlete newAthlete = new Athlete();
        newAthlete.setFullName(athleteDTO.getFullName());
        newAthlete.setNickname(athleteDTO.getNickname());
        newAthlete.setEnrollment(athleteDTO.getEnrollment());
        newAthlete.setPhone(athleteDTO.getPhone());
        newAthlete.setPass(athleteDTO.getPass());

        return athleteRepository.save(newAthlete);
    }
}
