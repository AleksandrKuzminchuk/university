package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class SubjectFindTeachersResponse {

    private SubjectDTO subject;
    private List<TeacherDTO> teachers;

    public SubjectFindTeachersResponse(SubjectDTO subject, List<TeacherDTO> teachers) {
        this.subject = subject;
        this.teachers = teachers;
    }
}
