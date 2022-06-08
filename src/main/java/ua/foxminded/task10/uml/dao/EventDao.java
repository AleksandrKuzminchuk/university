package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDao extends CrudRepositoryDao<Event, Integer> {

    void saveAll(List<Event> lessons);

    void updateEvent(Integer eventId, Event event);

    List<Event> findEvents(LocalDateTime from, LocalDateTime to);

}
