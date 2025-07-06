package ifs.championship.dto;

import ifs.championship.model.enums.CourseLevel;
import lombok.Data;

@Data
public class EventDTO {
    private String name;
    private CourseLevel level;
}