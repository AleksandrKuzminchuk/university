package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.SubjectDTO;

import java.util.List;

@Data
public class SubjectResponse {

    private List<SubjectDTO> subjects;

    public SubjectResponse(List<SubjectDTO> subjects) {
        this.subjects = subjects;
    }
}
