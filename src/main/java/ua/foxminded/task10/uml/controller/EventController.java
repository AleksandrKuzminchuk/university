package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.service.*;

import java.util.List;

@Controller
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor(onConstructor_= @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventController {

    EventService eventService;
    SubjectService subjectService;
    TeacherService teacherService;
    GroupService groupService;
    ClassroomService classroomService;

    @GetMapping()
    public String findAllEvents(Model model) {
        log.info("requested-> [GET]-'/events'");
        List<Event> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("count", events.size());
        log.info("FOUND {} EVENTS SUCCESSFULLY", events.size());
        return "events/events";
    }

    @GetMapping("/new")
    public String createFormForSaveEvent(Model model, @ModelAttribute("newEvent") Event event) {
        log.info("requested-> [GET]-'/new'");
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        return "events/formSaveEvent";
    }

    @PostMapping("/saved")
    public String saveEvent(Model model, @ModelAttribute Event event) {
        log.info("requested-> [POST]-'/saved'");
        Event savedEvent = eventService.save(getNewEvent(event));
        model.addAttribute("event", savedEvent);
        log.info("SAVED {} EVENT SUCCESSFULLY", savedEvent);
        return "events/fromSavedEvent";
    }

    @GetMapping("/{eventId}/update")
    public String createFormForUpdateEvent(Model model, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ModelAttribute("newEvent") Event event,
                                           @PathVariable("eventId") Integer eventId) {
        log.info("requested-> [GET]-'/{eventId}/update'");
        Event oldEvent = getEventById(eventId);
        model.addAttribute("oldEvent", oldEvent);
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        log.info("UPDATING... {}", oldEvent);
        return "events/formUpdateEvent";
    }

    @PatchMapping("/{eventId}/updated")
    public String updateEvent(Model model, @ModelAttribute Event event, @PathVariable("eventId") Integer eventId) {
        log.info("requested-> [PATCH]-'/{eventId}/updated'");
        eventService.updateEvent(eventId, getNewEvent(event));
        model.addAttribute("event", getEventById(eventId));
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
        return "events/formUpdatedEvent";
    }

    @DeleteMapping("/{eventId}/deleted")
    public String deleteEventById(Model model, @PathVariable("eventId") Integer eventId) {
        log.info("requested-> [DELETE]-'/{eventId}/deleted'");
        Event event = getEventById(eventId);
        eventService.deleteById(eventId);
        model.addAttribute("event", event);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventId);
        return "events/formDeletedEvent";
    }

    @GetMapping("/find")
    public String createFormForFindEvents(@ModelAttribute("event") Event event) {
        log.info("requested-> [GET]-'/find'");
        return "events/formForFindEvents";
    }

    @GetMapping("/found")
    public String findEvents(Model model, @ModelAttribute Event event) {
        log.info("requested-> [GET]->'/found'");
        List<Event> events = eventService.findEvents(event.getStartDateTime(), event.getEndDateTime());
        model.addAttribute("events", events);
        model.addAttribute("event", event);
        model.addAttribute("count", events.size());
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", events.size(), event.getStartDateTime(), event.getEndDateTime());
        return "events/formFoundEvents";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAllEvents(Model model) {
        log.info("requested-> [DELETE]-'delete_all_events'");
        Long countEvents = eventService.count();
        eventService.deleteAll();
        model.addAttribute("events", countEvents);
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return "events/formDeleteAllEvents";
    }


    private Event getNewEvent(Event event) {
        return new Event(
                subjectService.findById(event.getSubject().getId()),
                classroomService.findById(event.getClassroom().getId()),
                groupService.findById(event.getGroup().getId()),
                teacherService.findById(event.getTeacher().getId()),
                event.getDateTime()
        );
    }

    private void extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("groups", groupService.findAll());
    }

    private Event getEventById(Integer eventId) {
        return eventService.findById(eventId);
    }

}
