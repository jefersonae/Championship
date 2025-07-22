package ifs.championship.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AthleteRegisterDTO {
    private String fullName;
    private String nickName;
    private String phone;
    private String pass;
}