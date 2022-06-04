package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.service.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @Autowired
    public EventController(EventService eventService, SubjectService subjectService, TeacherService teacherService, GroupService groupService, ClassroomService classroomService) {
        this.eventService = eventService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.classroomService = classroomService;
    }

    @GetMapping("events")
    @ResponseStatus(HttpStatus.OK)
    public String findAllEvents(Model model) {
        logger.info("requested-> [GET]-'/events'");
        List<Event> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("count", events.size());
        logger.info("FOUND {} EVENTS SUCCESSFULLY", events.size());
        return "events/events";
    }

    @GetMapping("new_event")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveEvent(@ModelAttribute("newEvent") Event event) {
        logger.info("requested-> [GET]-'new_event'");
        return "events/formSaveEvent";
    }

    @PostMapping("saved_event")
    @ResponseStatus(HttpStatus.OK)
    public String saveEvent(Model model, @ModelAttribute Event event) {
        logger.info("requested-> [POST]-'saved_event'");
        Subject subject = subjectService.findSubjectByName(event.getSubject());
        Classroom classroom = classroomService.findClassroomByNumber(event.getClassroom().getNumber());
        Teacher teacher = teacherService.findTeacherByNameSurname(event.getTeacher());
        Group group = groupService.findByGroupName(event.getGroup().getName());
        Event newEvent = new Event();
        newEvent.setSubject(subject);
        newEvent.setClassroom(classroom);
        newEvent.setTeacher(teacher);
        newEvent.setGroup(group);
        newEvent.setDateTime(event.getDateTime());
        Event savedEvent = eventService.save(newEvent);
        model.addAttribute("event", savedEvent);
        return "events/fromSavedEvent";
    }

    @GetMapping("{eventId}/update_event")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateEvent(Model model, @PathVariable("eventId") Integer eventId) {
        logger.info("requested-> [GET]-'{eventId}/update_event'");
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        logger.info("UPDATING... {}", event);
        return "events/formUpdateEvent";
    }

    @PatchMapping("{eventId}/updated_event")
    @ResponseStatus(HttpStatus.OK)
    public String updateEvent(Model model, @ModelAttribute Event event, @PathVariable("eventId") Integer eventId) {
        logger.info("requested-> [PATCH]-'{eventId}/updated_event'");
        Event newEvent = eventService.findById(eventId);
        newEvent.getTeacher().setId(teacherService.findTeacherByNameSurname(event.getTeacher()).getId());
        newEvent.getClassroom().setId(classroomService.findClassroomByNumber(event.getClassroom().getNumber()).getId());
        newEvent.getSubject().setId(subjectService.findSubjectByName(event.getSubject()).getId());
        newEvent.getGroup().setId(groupService.findByGroupName(event.getGroup().getName()).getId());

        eventService.updateEvent(eventId, newEvent);
        model.addAttribute("event", newEvent);
        logger.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
        return "events/formUpdatedEvent";
    }

    @DeleteMapping("{eventId}/delete_event")
    @ResponseStatus(HttpStatus.OK)
    public String deleteEventById(Model model, @PathVariable("eventId") Integer eventId){
        logger.info("requested-> [DELETE]-'{eventId}/delete_event'");
        Event event = eventService.findById(eventId);
        eventService.deleteById(eventId);
        model.addAttribute("event", event);
        logger.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventId);
        return "events/formDeletedEvent";
    }

    @GetMapping("find_events")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindEvents(@ModelAttribute("event") Event event){
        logger.info("requested-> [GET]-'find_events'");
        return "events/formForFindEvents";
    }

    @GetMapping("found_events")
    @ResponseStatus(HttpStatus.OK)
    public String findEvents(Model model, @ModelAttribute Event event){
        logger.info("requested-> [GET]->'/found_events'");
        List<Event> events = eventService.findEvents(event.getStartDateTime(), event.getEndDateTime());
        model.addAttribute("events", events);
        model.addAttribute("event", event);
        model.addAttribute("count", events.size());
        logger.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", events.size(), event.getStartDateTime(), event.getEndDateTime());
        return "events/formFoundEvents";
    }

    @DeleteMapping("delete_all_events")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllEvents(){
        logger.info("requested-> [DELETE]-'delete_all_events'");
        eventService.deleteAll();
        logger.info("DELETED ALL EVENTS SUCCESSFULLY");
        return "events/formDeleteAllEvents";
    }
}
