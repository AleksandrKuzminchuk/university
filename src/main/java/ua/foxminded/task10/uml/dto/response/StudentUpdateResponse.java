package ua.foxminded.task10.uml.dto.response;

import lombok.Builder;
import lombok.Data;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;

import javax.validation.Valid;
import java.util.List;

@Data
@Builder
public class StudentUpdateResponse {
    @Valid
    private StudentDTO student;
    private List<GroupDTO> groups;
}
