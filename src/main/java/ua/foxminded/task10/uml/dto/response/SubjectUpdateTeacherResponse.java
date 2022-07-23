package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class SubjectUpdateTeacherResponse {

    private SubjectDTO subject;
    private TeacherDTO teacher;
    private List<TeacherDTO> teachers;

    public SubjectUpdateTeacherResponse(SubjectDTO subject, TeacherDTO teacher, List<TeacherDTO> teachers) {
        this.subject = subject;
        this.teacher = teacher;
        this.teachers = teachers;
    }
}
