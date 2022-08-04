package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.model.Group;

@Slf4j
@Component
public class EventMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public EventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Event map(EventDTO eventDTO){
        log.info("Mapping to event...");
        return modelMapper.map(eventDTO, Event.class);
    }

    public EventDTO map(Event event){
        log.info("Mapping to eventDTO...");
        return modelMapper.map(event, EventDTO.class);
    }

    public EventDTO map(EventCreateDTO eventCreateDTO){
        log.info("Mapping to EventDTO...");
        EventDTO eventDTO = new EventDTO();
        eventDTO.setDateTime(eventCreateDTO.getDateTime());
        eventDTO.setGroup(new GroupDTO(eventCreateDTO.getGroupId()));
        eventDTO.setClassroom(new ClassroomDTO(eventCreateDTO.getClassroomId()));
        eventDTO.setSubject(new SubjectDTO(eventCreateDTO.getSubjectId()));
        eventDTO.setTeacher(new TeacherDTO(eventCreateDTO.getTeacherId()));
        return eventDTO;
    }
}
