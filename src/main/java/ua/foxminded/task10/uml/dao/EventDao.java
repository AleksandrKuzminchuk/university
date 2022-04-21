package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Event;

import java.util.List;
import java.util.Optional;

public interface EventDao extends CrudRepository<Event, Integer>{

    void saveAll(List<Event> events);

    Optional<Event> findEventByEventName(String eventName);

    void updateEvent(Event event);
}
