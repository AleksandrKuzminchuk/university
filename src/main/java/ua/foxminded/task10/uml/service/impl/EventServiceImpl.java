package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.repository.*;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    TeacherRepository teacherRepository;
    GroupRepository groupRepository;
    SubjectRepository subjectRepository;
    ClassroomRepository classroomRepository;

    @Override
    public Event save(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        log.info("SAVING... {}", event);
        eventRepository.save(event);
        log.info("SAVED {} SUCCESSFULLY", event);
        return event;
    }

    @Override
    public Event findById(Integer eventId) {
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("FINDING... EVENT BY ID - {}", eventId);
        Event result = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(format("Can't find event by eventId - %d", eventId)));
        log.info("FOUND {} BY ID - {}", result, eventId);
        return result;
    }

    @Override
    public boolean existsById(Integer eventId) {
        requireNonNull(eventId);
        log.info("CHECKING... EVENT BY ID - {}", eventId);
        boolean result = eventRepository.existsById(eventId);
        log.info("EVENT BY ID - {} EXISTS - {}", eventId, result);
        return result;
    }

    @Override
    public List<Event> findAll() {
        log.info("FINDING... ALL EVENTS");
        List<Event> result = eventRepository.findAll(Sort.by(Sort.Order.asc("dateTime")));
        log.info("FOUND {} EVENTS", result.size());
        return result;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT EVENTS");
        long result = eventRepository.count();
        log.info("FOUND {} EVENTS", result);
        return result;
    }

    @Override
    public void deleteById(Integer eventId) {
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("DELETING... EVENT BY ID - {}", eventId);
        eventRepository.deleteById(eventId);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        requiredEventExistence(event);
        log.info("DELETING... {}", event);
        eventRepository.deleteById(event.getId());
        log.info("DELETED {} SUCCESSFULLY", event);
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL EVENTS");
        eventRepository.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        events.forEach(this::requiredEventExistence);
        log.info("SAVING... {} EVENTS", events.size());
        eventRepository.saveAll(events);
        log.info("SAVED {} EVENTS SUCCESSFULLY", events.size());
    }

    @Override
    public Event update(Event event) {
        requireNonNull(event.getId());
        requiredEventByIdExistence(event.getId());
        log.info("UPDATING... EVENT BY ID - {}", event.getId());
        Event updatedEvent = eventRepository.save(event);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", event.getId());
        return updatedEvent;
    }

    @Override
    public List<Event> findEvents(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        requireNonNull(startDateTime);
        requireNonNull(endDateTime);
        log.info("FINDING... EVENT FROM {} TO {}", startDateTime.format(formatter), endDateTime.format(formatter));
        List<Event> result = eventRepository.findEventsByDateTimeOrderByDateTime(startDateTime, endDateTime);
        log.info("FOUND {} EVENT FROM {} TO {}", result.size(), startDateTime.format(formatter), endDateTime.format(formatter));
        return result;
    }

    private void requiredEventExistence(Event event) {
        if (!teacherRepository.existsById(event.getTeacher().getId())) {
            throw new NotFoundException(format("Teacher by id - %d not exists", event.getTeacher().getId()));
        } else if (!groupRepository.existsById(event.getGroup().getId())) {
            throw new NotFoundException(format("Group by id - %d not exists", event.getGroup().getId()));
        } else if (!subjectRepository.existsById(event.getSubject().getId())) {
            throw new NotFoundException(format("Subject by id - %d not exists", event.getSubject().getId()));
        } else if (!classroomRepository.existsById(event.getClassroom().getId())) {
            throw new NotFoundException(format("Classroom by id - %d not exists", event.getClassroom().getId()));
        }
    }

    private void requiredEventByIdExistence(Integer eventId){
        if (!eventRepository.existsById(eventId)){
            throw new NotFoundException(format("Event by id- %d not exists", eventId));
        }
    }
}
