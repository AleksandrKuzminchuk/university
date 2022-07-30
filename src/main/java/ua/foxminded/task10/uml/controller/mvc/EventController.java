package ua.foxminded.task10.uml.controller.mvc;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.EventExtraDTO;
import ua.foxminded.task10.uml.dto.response.EventUpdateSaveResponse;
import ua.foxminded.task10.uml.service.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventController {

    private final EventService eventService;

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
    public String saveForm(Model model) {
        log.info("requested-> [GET]-'/events/new'");
        EventUpdateSaveResponse saveEvent = eventService.saveForm();
        model.addAttribute("saveEvent", saveEvent);
        model.addAttribute("event", new EventDTO());
        return "events/formSaveEvent";
    }

    @PostMapping("/saved")
    public String save(@ModelAttribute EventDTO eventDTO) {
        log.info("requested-> [POST]-'/events/saved'");
        EventDTO savedEventDTO = eventService.save(eventDTO);
        log.info("SAVED {} EVENT SUCCESSFULLY", savedEventDTO);
        return "events/fromSavedEvent";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/events/{id}/update'");
        EventUpdateSaveResponse updateEvent = eventService.updateForm(id);
        model.addAttribute("updateEvent", updateEvent);
        model.addAttribute("event", new EventDTO());
        log.info("UPDATING... EVENT BY ID - {}", id);
        return "events/formUpdateEvent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model,
                         @ModelAttribute EventDTO eventDTO,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/events/{id}/updated'");
        eventDTO.setId(id);
        eventService.update(eventDTO);
        model.addAttribute("event", eventDTO);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", id);
        return "events/formUpdatedEvent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/events/{id}/deleted'");
        eventService.deleteById(id);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", id);
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
    public String deleteAll() {
        log.info("requested-> [DELETE]-'/events/deleted/all'");
        eventService.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return "events/formDeleteAllEvents";
    }

}
