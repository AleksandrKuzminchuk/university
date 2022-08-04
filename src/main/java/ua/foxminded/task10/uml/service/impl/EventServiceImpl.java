package ua.foxminded.task10.uml.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.dto.mapper.*;
import ua.foxminded.task10.uml.dto.response.EventUpdateSaveResponse;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.repository.*;
import ua.foxminded.task10.uml.service.*;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotFoundException;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotNullException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ua.foxminded.task10.uml.util.formatters.DateTimeFormat.formatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ClassroomService classroomService;
    private final EventMapper mapper;

    @Override
    public EventUpdateSaveResponse saveForm() {
        log.info("PREPARING SAVE FORM EVENT");
        List<SubjectDTO> subjectsDTO = subjectService.findAll();
        List<ClassroomDTO> classroomsDTO = classroomService.findAll();
        List<TeacherDTO> teachersDTO = teacherService.findAll();
        List<GroupDTO> groupsDTO = groupService.findAll();
        EventUpdateSaveResponse response = new EventUpdateSaveResponse(new EventDTO(), subjectsDTO, classroomsDTO, teachersDTO, groupsDTO);
        log.info("PREPARED SAVE FROM SUCCESSFULLY");
        return response;
    }

    @Override
    public EventDTO save(EventDTO eventDTO) {
        requiredEventExistence(eventDTO);
        log.info("SAVING... {}", eventDTO);
        Event event = mapper.map(eventDTO);
        Event savedEvent = eventRepository.save(event);
        EventDTO savedEventDTO = mapper.map(savedEvent);
        log.info("SAVED {} SUCCESSFULLY", savedEventDTO);
        return savedEventDTO;
    }

    @Override
    public EventDTO findById(Integer eventId) {
        requireNonNull(eventId);
        requiredEventByIdExistence(eventId);
        log.info("FINDING... EVENT BY ID - {}", eventId);
        Event result = eventRepository.findById(eventId).
                orElseThrow(() -> new GlobalNotFoundException(format("Can't find event by eventId - %d", eventId)));
        EventDTO eventDTO = mapper.map(result);
        log.info("FOUND {} BY ID - {}", eventDTO, eventId);
        return eventDTO;
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
    public List<EventDTO> findAll() {
        log.info("FINDING... ALL EVENTS");
        List<Event> result = eventRepository.findAll(Sort.by(Sort.Order.asc("dateTime")));
        List<EventDTO> eventsDTO= result.stream().map(mapper::map).collect(Collectors.toList());
        log.info("FOUND {} EVENTS", result.size());
        return eventsDTO;
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
    public void delete(EventDTO eventDTO) {
        requireNonNull(eventDTO);
        requiredEventExistence(eventDTO);
        log.info("DELETING... {}", eventDTO);
        eventRepository.deleteById(eventDTO.getId());
        log.info("DELETED {} SUCCESSFULLY", eventDTO);
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL EVENTS");
        eventRepository.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<EventDTO> eventsDTO) {
        requireNonNull(eventsDTO);
        eventsDTO.forEach(this::requiredEventExistence);
        log.info("SAVING... {} EVENTS", eventsDTO.size());
        List<Event> events = eventsDTO.stream().map(mapper::map).collect(Collectors.toList());
        eventRepository.saveAll(events);
        log.info("SAVED {} EVENTS SUCCESSFULLY", events.size());
    }

    @Override
    public EventUpdateSaveResponse updateForm(Integer id) {
        log.info("PREPARING UPDATE FORM EVENT BY ID - {}", id);
        requiredEventByIdExistence(id);
        EventDTO eventDTO = this.findById(id);
        List<SubjectDTO> subjectsDTO = subjectService.findAll();
        List<ClassroomDTO> classroomsDTO = classroomService.findAll();
        List<TeacherDTO> teachersDTO = teacherService.findAll();
        List<GroupDTO> groupsDTO = groupService.findAll();
        EventUpdateSaveResponse response = new EventUpdateSaveResponse(eventDTO, subjectsDTO, classroomsDTO, teachersDTO, groupsDTO);
        log.info("PREPARED UPDATE FROM SUCCESSFULLY");
        return response;
    }

    @Override
    public void update(EventDTO eventDTO) {
        requireNonNull(eventDTO.getId());
        requiredEventByIdExistence(eventDTO.getId());
        log.info("UPDATING... EVENT BY ID - {}", eventDTO.getId());
        Event event = mapper.map(eventDTO);
        Event updatedEvent = eventRepository.save(event);
        mapper.map(updatedEvent);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventDTO.getId());
    }

    @Override
    public List<EventDTO> find(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        requireNonNull(startDateTime);
        requireNonNull(endDateTime);
        log.info("FINDING... EVENT FROM {} TO {}", startDateTime.format(formatter), endDateTime.format(formatter));
        List<Event> result = eventRepository.findByDateTimeOrderByDateTime(startDateTime, endDateTime);
        List<EventDTO> eventsDTO = result.stream().map(mapper::map).collect(Collectors.toList());
        log.info("FOUND {} EVENT FROM {} TO {}", result.size(), startDateTime.format(formatter), endDateTime.format(formatter));
        return eventsDTO;
    }

    private void requiredEventExistence(EventDTO eventDTO) {
        if (!teacherService.existsById(eventDTO.getTeacher().getId())) {
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", eventDTO.getTeacher().getId()));
        } else if (!groupService.existsById(eventDTO.getGroup().getId())) {
            throw new GlobalNotFoundException(format("Group by id - %d not exists", eventDTO.getGroup().getId()));
        } else if (!subjectService.existsById(eventDTO.getSubject().getId())) {
            throw new GlobalNotFoundException(format("Subject by id - %d not exists", eventDTO.getSubject().getId()));
        } else if (!classroomService.existsById(eventDTO.getClassroom().getId())) {
            throw new GlobalNotFoundException(format("Classroom by id - %d not exists", eventDTO.getClassroom().getId()));
        }
    }

    private void requiredEventByIdExistence(Integer eventId){
        if (!eventRepository.existsById(eventId)){
            throw new GlobalNotFoundException(format("Event by id- %d not exists", eventId));
        }
    }

    private void requireNonNull(Object o){
        if (o == null){
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
