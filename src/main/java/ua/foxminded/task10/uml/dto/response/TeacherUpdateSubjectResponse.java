package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class TeacherUpdateSubjectResponse {

    private TeacherDTO teacher;
    private SubjectDTO subject;
    private List<SubjectDTO> subjects;

    public TeacherUpdateSubjectResponse(TeacherDTO teacher, SubjectDTO subject, List<SubjectDTO> subjects) {
        this.teacher = teacher;
        this.subject = subject;
        this.subjects = subjects;
    }
}
