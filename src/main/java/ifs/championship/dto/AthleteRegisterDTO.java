package ifs.championship.dto;

import lombok.Data;

@Data
public class AthleteRegisterDTO {
    private String fullName;
    private String nickname;
    private String phone;
    private String pass;
    private Long enrollment;
}
