package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.service.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventController {

    private final EventService eventService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @GetMapping()
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/events'");
        List<Event> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("count", events.size());
        log.info("FOUND {} EVENTS SUCCESSFULLY", events.size());
        return "events/events";
    }

    @GetMapping("/new")
    public String saveForm(Model model, @ModelAttribute("newEvent") Event event) {
        log.info("requested-> [GET]-'/new'");
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        return "events/formSaveEvent";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute Event event) {
        log.info("requested-> [POST]-'/saved'");
        Event savedEvent = eventService.save(event);
        model.addAttribute("event", savedEvent);
        log.info("SAVED {} EVENT SUCCESSFULLY", savedEvent);
        return "events/fromSavedEvent";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @ModelAttribute("newEvent") Event event, @PathVariable("id") Integer eventId) {
        log.info("requested-> [GET]-'/{id}/update'");
        Event oldEvent = getEventById(eventId);
        model.addAttribute("oldEvent", oldEvent);
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        log.info("UPDATING... {}", oldEvent);
        return "events/formUpdateEvent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @ModelAttribute Event event, @PathVariable("id") Integer eventId) {
        log.info("requested-> [PATCH]-'/{id}/updated'");
        event.setId(eventId);
        Event updatedEvent = eventService.update(event);
        model.addAttribute("event", updatedEvent);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
        return "events/formUpdatedEvent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model, @PathVariable("id") Integer eventId) {
        log.info("requested-> [DELETE]-'/{id}/deleted'");
        Event event = getEventById(eventId);
        eventService.deleteById(eventId);
        model.addAttribute("event", event);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", event);
        return "events/formDeletedEvent";
    }

    @GetMapping("/find")
    public String findEventsForm(@ModelAttribute("event") Event event) {
        log.info("requested-> [GET]-'/find'");
        return "events/formForFindEvents";
    }

    @GetMapping("/found")
    public String findEvents(Model model, @ModelAttribute Event event) {
        log.info("requested-> [GET]->'/found'");
        List<Event> events = eventService.find(event.getStartDateTime(), event.getEndDateTime());
        model.addAttribute("events", events);
        model.addAttribute("event", event);
        model.addAttribute("count", events.size());
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", events.size(), event.getStartDateTime(), event.getEndDateTime());
        return "events/formFoundEvents";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested-> [DELETE]-'delete_all_events'");
        Long countEvents = eventService.count();
        eventService.deleteAll();
        model.addAttribute("events", countEvents);
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return "events/formDeleteAllEvents";
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
