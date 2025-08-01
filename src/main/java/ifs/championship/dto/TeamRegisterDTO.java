package ifs.championship.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamRegisterDTO {
    private String teamName;
    private Long courseId;
    private Long sportId;
    private Long captainId;
    private List<Long> athleteEnrollment; // Array of athlete IDs to be enrolled in the team
}