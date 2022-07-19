package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class TeacherResponse {

    private List<TeacherDTO> teachers;

    public TeacherResponse(List<TeacherDTO> teachers) {
        this.teachers = teachers;
    }
}
