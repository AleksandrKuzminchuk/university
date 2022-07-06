package ua.foxminded.task10.uml.service;



import ua.foxminded.task10.uml.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CrudRepositoryService<Event, Integer> {

    void saveAll(List<Event> events);

    Event update(Event event);

    List<Event> findEvents(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
