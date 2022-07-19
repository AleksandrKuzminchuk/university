package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.EventExtraDTO;
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
        List<EventDTO> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("count", events.size());
        log.info("FOUND {} EVENTS SUCCESSFULLY", events.size());
        return "events/events";
    }

    @GetMapping("/new")
    public String saveForm(Model model, @ModelAttribute("newEvent") EventDTO eventDTO) {
        log.info("requested-> [GET]-'/events/new'");
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        return "events/formSaveEvent";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute EventDTO eventDTO) {
        log.info("requested-> [POST]-'/events/saved'");
        EventDTO savedEventDTO = eventService.save(eventDTO);
        model.addAttribute("event", savedEventDTO);
        log.info("SAVED {} EVENT SUCCESSFULLY", savedEventDTO);
        return "events/fromSavedEvent";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @ModelAttribute("newEvent") EventDTO eventDTO, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/events/{id}/update'");
        EventDTO oldEventDTO = getEventById(id);
        model.addAttribute("oldEvent", oldEventDTO);
        extractedMethodFindAllSubjectsClassroomsTeachersGroupsToModel(model);
        log.info("UPDATING... {}", oldEventDTO);
        return "events/formUpdateEvent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @ModelAttribute EventDTO eventDTO, @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/events/{id}/updated'");
        eventDTO.setId(id);
        EventDTO updatedEvent = eventService.update(eventDTO);
        model.addAttribute("event", updatedEvent);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", id);
        return "events/formUpdatedEvent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/events/{id}/deleted'");
        EventDTO eventDTO = getEventById(id);
        eventService.deleteById(id);
        model.addAttribute("event", eventDTO);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventDTO);
        return "events/formDeletedEvent";
    }

    @GetMapping("/find")
    public String findEventsForm(@ModelAttribute("event") EventDTO eventDTO) {
        log.info("requested-> [GET]-'/events/find'");
        return "events/formForFindEvents";
    }

    @GetMapping("/found")
    public String findEvents(Model model, @ModelAttribute EventExtraDTO eventExtraDTO) {
        log.info("requested-> [GET]->'/events/found'");
        List<EventDTO> eventsDTO = eventService.find(eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        model.addAttribute("events", eventsDTO);
        model.addAttribute("event", eventExtraDTO);
        model.addAttribute("count", eventsDTO.size());
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", eventsDTO.size(), eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        return "events/formFoundEvents";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested-> [DELETE]-'/events/deleted/all'");
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

    private EventDTO getEventById(Integer eventId) {
        return eventService.findById(eventId);
    }

}
