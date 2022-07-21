package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

@Data
public class StudentUpdateResponse {
    private Student student;
    private List<GroupDTO> groups;

    public StudentUpdateResponse(Student student, List<GroupDTO> groups) {
    }
}
