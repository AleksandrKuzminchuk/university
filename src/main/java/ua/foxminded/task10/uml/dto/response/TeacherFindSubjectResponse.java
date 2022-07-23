package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

@Data
public class TeacherFindSubjectResponse {

    private TeacherDTO teacher;
    private List<SubjectDTO> subjects;

    public TeacherFindSubjectResponse(TeacherDTO teacher, List<SubjectDTO> subjects) {
        this.teacher = teacher;
        this.subjects = subjects;
    }
}
