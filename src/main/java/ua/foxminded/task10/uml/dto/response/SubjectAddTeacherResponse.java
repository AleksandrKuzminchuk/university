package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class SubjectAddTeacherResponse {

    private SubjectDTO subject;
    private TeacherDTO teacher;
    List<TeacherDTO> teachers;

    public SubjectAddTeacherResponse(SubjectDTO subject, List<TeacherDTO> teachers) {
        this.subject = subject;
        this.teachers = teachers;
    }
}
