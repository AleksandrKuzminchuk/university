package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.model.Subject;

@Slf4j
@Component
public class SubjectMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public SubjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Subject map(SubjectDTO subjectDTO){
        log.info("Mapping to subject...");
        return modelMapper.map(subjectDTO, Subject.class);
    }

    public SubjectDTO map(Subject subject){
        log.info("Mapping to subjectDTO...");
        return modelMapper.map(subject, SubjectDTO.class);
    }
}
