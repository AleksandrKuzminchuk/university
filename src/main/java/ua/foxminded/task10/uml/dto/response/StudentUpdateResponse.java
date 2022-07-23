package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

@Data
public class StudentUpdateResponse {
    private StudentDTO student;
    private List<GroupDTO> groups;

    public StudentUpdateResponse(StudentDTO student, List<GroupDTO> groups) {
        this.student = student;
        this.groups = groups;
    }
}
