package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CrudRepositoryService<EventDTO, Integer> {

    Integer saveAll(List<EventDTO> events);

    EventDTO update(EventDTO eventDTO);

    List<EventDTO> find(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
