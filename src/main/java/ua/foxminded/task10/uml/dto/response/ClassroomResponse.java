package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.ClassroomDTO;

import java.util.List;

@Data
public class ClassroomResponse {

    private List<ClassroomDTO> classrooms;

    public ClassroomResponse(List<ClassroomDTO> classrooms) {
        this.classrooms = classrooms;
    }
}
