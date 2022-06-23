package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dao.*;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_= {@Autowired})
@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    EventDao eventDao;
    TeacherDao teacherDao;
    GroupDao groupDao;
    SubjectDao subjectDao;
    ClassroomDao classroomDao;

    @Transactional
    @Override
    public Event save(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        log.info("SAVING... {}", event);
        Event result = eventDao.save(event).orElseThrow(() -> new NotFoundException(format("Can't save %s", event)));
        log.info("SAVED {} SUCCESSFULLY", event);
        return result;
    }

    @Override
    public Event findById(Integer eventId) {
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("FINDING... EVENT BY ID - {}", eventId);
        Event result = eventDao.findById(eventId).orElseThrow(() -> new NotFoundException(format("Can't find event by eventId - %d", eventId)));
        log.info("FOUND {} BY ID - {}", result, eventId);
        return result;
    }

    @Override
    public boolean existsById(Integer eventId) {
        requireNonNull(eventId);
        log.info("CHECKING... EVENT BY ID - {}", eventId);
        boolean result = eventDao.existsById(eventId);
        log.info("EVENT BY ID - {} EXISTS - {}", eventId, result);
        return result;
    }

    @Override
    public List<Event> findAll() {
        log.info("FINDING... ALL EVENTS");
        List<Event> result = eventDao.findAll();
        log.info("FOUND {} EVENTS", result.size());
        return result;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT EVENTS");
        long result = eventDao.count();
        log.info("FOUND {} EVENTS", result);
        return result;
    }

    @Transactional
    @Override
    public void deleteById(Integer eventId) {
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("DELETING... EVENT BY ID - {}", eventId);
        eventDao.deleteById(eventId);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Transactional
    @Override
    public void delete(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        log.info("DELETING... {}", event);
        eventDao.deleteById(event.getId());
        log.info("DELETED {} SUCCESSFULLY", event);
    }

    @Transactional
    @Override
    public void deleteAll() {
        log.info("DELETING... ALL EVENTS");
        eventDao.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        events.forEach(this::requiredEventExistence);
        log.info("SAVING... {} EVENTS", events.size());
        eventDao.saveAll(events);
        log.info("SAVED {} EVENTS SUCCESSFULLY", events.size());
    }

    @Transactional
    @Override
    public void updateEvent(Integer eventId, Event event) {
        requireNonNull(event);
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("UPDATING... EVENT BY ID - {}", eventId);
        eventDao.updateEvent(eventId, event);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        log.info("FINDING... EVENT FROM {} TO {}", from.format(formatter), to.format(formatter));
        List<Event> result = eventDao.findEvents(from, to);
        log.info("FOUND {} EVENT FROM {} TO {}", result.size(), from.format(formatter), to.format(formatter));
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
