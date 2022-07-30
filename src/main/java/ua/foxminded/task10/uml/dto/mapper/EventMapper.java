package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.model.Event;

@Slf4j
@Component
public class EventMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public EventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Event convertToEvent(EventDTO eventDTO){
        log.info("Mapping to event...");
        return modelMapper.map(eventDTO, Event.class);
    }

    public EventDTO convertToEventDTO(Event event){
        log.info("Mapping to eventDTO...");
        return modelMapper.map(event, EventDTO.class);
    }
}
