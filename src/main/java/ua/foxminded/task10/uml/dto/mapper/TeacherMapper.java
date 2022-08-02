package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.model.Teacher;

@Slf4j
@Component
public class TeacherMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TeacherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Teacher map(TeacherDTO teacherDTO){
        log.info("Mapping to teacher...");
        return modelMapper.map(teacherDTO, Teacher.class);
    }

    public TeacherDTO map(Teacher teacher){
        log.info("Mapping to teacherDTO...");
        return modelMapper.map(teacher, TeacherDTO.class);
    }
}
