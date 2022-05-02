package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Event;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public interface EventDao extends CrudRepository<Event, Integer>{

    void saveAll(List<Event> lessons);

    void updateLesson(Event event);

    List<Event> findEvents(LocalDateTime from, LocalDateTime to);

}
