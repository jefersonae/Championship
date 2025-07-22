package ifs.championship.service;

import ifs.championship.dto.AthleteRegisterDTO;
import ifs.championship.model.Athlete;
import ifs.championship.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AthleteService {
    @Autowired
    private AthleteRepository athleteRepository;

    public Athlete createAthlete(AthleteRegisterDTO athleteDTO) {
        Athlete newAthlete = new Athlete();
        newAthlete.setFullName(athleteDTO.getFullName());
        newAthlete.setNickName(athleteDTO.getNickName());
        newAthlete.setPhone(athleteDTO.getPhone());
        newAthlete.setPass(athleteDTO.getPass());

        return athleteRepository.save(newAthlete);
    }
}
