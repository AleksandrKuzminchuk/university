package ua.foxminded.task10.uml.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.EventExtraDTO;
import ua.foxminded.task10.uml.dto.response.EventResponse;
import ua.foxminded.task10.uml.service.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventRestController {

    private final EventService eventService;

    @GetMapping()
    public EventResponse findAll() {
        log.info("requested-> [GET]-'/api/events'");
        return new EventResponse(eventService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<EventDTO> save(@RequestBody EventDTO eventDTO) {
        log.info("requested-> [POST]-'/api/events/saved'");
        EventDTO savedEvent = eventService.save(eventDTO);
        log.info("SAVED {} EVENT SUCCESSFULLY", eventDTO);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<EventDTO> update(@RequestBody EventDTO eventDTO) {
        log.info("requested-> [PATCH]-'/api/events/{id}/updated'");
        EventDTO updatedEvent = eventService.update(eventDTO);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", updatedEvent);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<EventDTO> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/events/{id}/deleted'");
        EventDTO eventDTO = eventService.findById(id);
        eventService.deleteById(id);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> deleteAll() {
        log.info("requested-> [DELETE]-'/api/events/delete/all'");
        Long countEvents = eventService.count();
        eventService.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return new ResponseEntity<>(countEvents, HttpStatus.OK);
    }

    @GetMapping("/find")
    public EventResponse findEvents(@RequestBody EventExtraDTO eventExtraDTO) {
        log.info("requested-> [GET]->'/api/events/find'");
        List<EventDTO> eventsDTO = eventService.find(eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", eventsDTO.size(), eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        return new EventResponse(eventsDTO);
    }

}
