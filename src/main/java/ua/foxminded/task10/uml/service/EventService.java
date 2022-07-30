package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.response.EventUpdateSaveResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CrudRepositoryService<EventDTO, Integer> {

    void saveAll(List<EventDTO> events);

    void update(EventDTO eventDTO);

    List<EventDTO> find(LocalDateTime startDateTime, LocalDateTime endDateTime);

    EventUpdateSaveResponse updateForm(Integer id);

    EventUpdateSaveResponse saveForm();
}
