package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.model.Student;

@Slf4j
@Component
public class StudentMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public StudentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Student map(StudentDTO studentDTO){
        log.info("Mapping to student...");
        return modelMapper.map(studentDTO, Student.class);
    }

    public StudentDTO map(Student student){
        log.info("Mapping to studentDTO...");
        return modelMapper.map(student, StudentDTO.class);
    }
}
