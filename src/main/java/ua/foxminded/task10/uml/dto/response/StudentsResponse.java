package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.StudentDTO;

import java.util.List;

@Data
public class StudentsResponse {

    private List<StudentDTO> students;

    public StudentsResponse(List<StudentDTO> students) {
        this.students = students;
    }
}
