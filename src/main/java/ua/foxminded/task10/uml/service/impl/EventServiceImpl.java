package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.*;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@Component
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventDao eventDao;
    private final TeacherDao teacherDao;
    private final GroupDao groupDao;
    private final SubjectDao subjectDao;
    private final ClassroomDao classroomDao;

    @Autowired
    public EventServiceImpl(EventDao eventDao, TeacherDao teacherDao, GroupDao groupDao, SubjectDao subjectDao, ClassroomDao classroomDao) {
        this.eventDao = eventDao;
        this.teacherDao = teacherDao;
        this.groupDao = groupDao;
        this.subjectDao = subjectDao;
        this.classroomDao = classroomDao;
    }

    @Override
    public Event save(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        logger.info("SAVING... {}", event);
        Event result = eventDao.save(event).orElseThrow(() -> new NotFoundException(format("Can't save %s", event)));
        logger.info("SAVED {} SUCCESSFULLY", event);
        return result;
    }

    @Override
    public Event findById(Integer id) {
        requireNonNull(id);
        requiredEventByIdExistence(id);
        logger.info("FINDING... EVENT BY ID - {}", id);
        Event result = eventDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find event by id - %d", id)));
        logger.info("FOUND {} BY ID - {}", result, id);
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... EVENT BY ID - {}", id);
        boolean result = eventDao.existsById(id);
        logger.info("EVENT BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Event> findAll() {
        logger.info("FINDING... ALL EVENTS");
        List<Event> result = eventDao.findAll();
        logger.info("FOUND {} EVENTS", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT EVENTS");
        long result = eventDao.count();
        logger.info("FOUND {} EVENTS", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        requiredEventByIdExistence(id);
        logger.info("DELETING... EVENT BY ID - {}", id);
        eventDao.deleteById(id);
        logger.info("DELETED EVENT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        logger.info("DELETING... {}", event);
        eventDao.deleteById(event.getId());
        logger.info("DELETED {} SUCCESSFULLY", event);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL EVENTS");
        eventDao.deleteAll();
        logger.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        events.forEach(this::requiredEventExistence);
        logger.info("SAVING... {} EVENTS", events.size());
        eventDao.saveAll(events);
        logger.info("SAVED {} EVENTS SUCCESSFULLY", events.size());
    }

    @Override
    public void updateEvent(Integer eventId, Event event) {
        requireNonNull(event);
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        logger.info("UPDATING... EVENT BY ID - {}", eventId);
        eventDao.updateEvent(eventId, event);
        logger.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        logger.info("FINDING... EVENT FROM {} TO {}", from.format(formatter), to.format(formatter));
        List<Event> result = eventDao.findEvents(from, to);
        logger.info("FOUND {} EVENT FROM {} TO {}", result.size(), from.format(formatter), to.format(formatter));
        return result;
    }

    private void requiredEventExistence(Event event) {
        if (!teacherDao.existsById(event.getTeacher().getId())) {
            throw new NotFoundException(format("Teacher by id - %d not exists", event.getTeacher().getId()));
        } else if (!groupDao.existsById(event.getGroup().getId())) {
            throw new NotFoundException(format("Group by id - %d not exists", event.getGroup().getId()));
        } else if (!subjectDao.existsById(event.getSubject().getId())) {
            throw new NotFoundException(format("Subject by id - %d not exists", event.getSubject().getId()));
        } else if (!classroomDao.existsById(event.getClassroom().getId())) {
            throw new NotFoundException(format("Classroom by id - %d not exists", event.getClassroom().getId()));
        }
    }

    private void requiredEventByIdExistence(Integer eventId){
        if (!eventDao.existsById(eventId)){
            throw new NotFoundException(format("Event by id- %d not exists", eventId));
        }
    }
}
