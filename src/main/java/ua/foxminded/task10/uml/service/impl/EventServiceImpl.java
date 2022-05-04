package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

public class EventServiceImpl implements EventService {

    private static final Logger logger = Logger.getLogger(EventServiceImpl.class);

    private final EventDao eventDao;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ClassroomService classroomService;

    public EventServiceImpl(EventDao eventDao, TeacherService teacherService, GroupService groupService, SubjectService subjectService, ClassroomService classroomService) {
        this.eventDao = eventDao;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.classroomService = classroomService;
    }

    @Override
    public Event save(Event event) {
        requireNonNull(event);
        getExceptionNotFound(event);
        logger.info(format("SAVING... %s", event));
        Event result = eventDao.save(event).orElseThrow(() -> new NotFoundException(format("Can't save %s", event)));
        logger.info(format("SAVED %s SUCCESSFULLY", event));
        return result;
    }

    @Override
    public Event findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... EVENT BY ID - %d", id));
        Event result = eventDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find event by id - %d", id)));
        logger.info(format("FOUND %s BY ID - %d", result, id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... EVENT BY ID - %d", id));
        boolean result = eventDao.existsById(id);
        logger.info(format("EVENT BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Event> findAll() {
        logger.info("FINDING... ALL EVENTS");
        List<Event> result = eventDao.findAll();
        if (result.isEmpty()){
            logger.info(format("FOUND %d EVENTS", 0));
            return result;
        }
        logger.info(format("FOUND %d EVENTS", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT EVENTS");
        long result = eventDao.count();
        logger.info(format("FOUND %d EVENTS", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... EVENT BY ID - %d", id));
        eventDao.deleteById(id);
        logger.info(format("DELETED EVENT BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        logger.info(format("DELETING... %s", event));
        eventDao.deleteById(event.getId());
        logger.info(format("DELETED %s SUCCESSFULLY", event));
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
        for(Event event: events){
            getExceptionNotFound(event);
        }
        logger.info(format("SAVING... %d EVENTS", events.size()));
        eventDao.saveAll(events);
        logger.info(format("SAVED %d EVENTS SUCCESSFULLY", events.size()));
    }

    @Override
    public void updateEvent(Event event) {
        requireNonNull(event);
        getExceptionNotFound(event);
        logger.info(format("UPDATING... %s", event));
        eventDao.updateEvent(event);
        logger.info(format("UPDATED %s SUCCESSFULLY", event));
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        logger.info(format("FINDING... EVENT FROM %s TO %s", from.format(formatter), to.format(formatter)));
        List<Event> result = eventDao.findEvents(from, to);
        if (result.isEmpty()){
            logger.info(format("FOUND %d EVENTS FROM %s TO %s", 0, from.format(formatter), to.format(formatter)));
            return result;
        }
        logger.info(format("FOUND %d EVENT FROM %s TO %s", result.size(), from.format(formatter), to.format(formatter)));
        return result;
    }

    private void getExceptionNotFound(Event event){
        if (!teacherService.existsById(event.getTeacher().getId()) ||
                !groupService.existsById(event.getGroup().getId()) ||
                !subjectService.existsById(event.getSubject().getId()) ||
                !classroomService.existsById(event.getClassroom().getId())) {
            throw new NotFoundException(format("Teacher by id - %d or Group by id - %d or Subject by id - %d " +
                            "or Classroom by id - %d not exists", event.getTeacher().getId(), event.getGroup().getId(),
                    event.getSubject().getId(), event.getClassroom().getId()));
        }
    }
}
