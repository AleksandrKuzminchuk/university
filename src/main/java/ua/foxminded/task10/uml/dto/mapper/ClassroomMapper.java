package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.ClassroomCreateDTO;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.model.Classroom;

@Slf4j
@Component
public class ClassroomMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ClassroomMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Classroom convertToClassroom(ClassroomDTO classroomDTO){
        log.info("Mapping to classroom...");
        return modelMapper.map(classroomDTO, Classroom.class);
    }

    public ClassroomDTO convertToClassroomDTO(Classroom classroom){
        log.info("Mapping to classroomDTO...");
        return modelMapper.map(classroom, ClassroomDTO.class);
    }

    public ClassroomDTO convertToClassroomDTO(ClassroomCreateDTO classroomCreateDTO){
        log.info("Mapping to classroomDTO...");
        return modelMapper.map(classroomCreateDTO, ClassroomDTO.class);
    }
}
