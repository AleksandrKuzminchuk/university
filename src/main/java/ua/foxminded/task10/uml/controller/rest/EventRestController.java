package ua.foxminded.task10.uml.controller.rest;

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
        List<EventDTO> eventsDTO = eventService.findAll();
        return new EventResponse(eventsDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<EventDTO> save(@RequestBody EventDTO eventDTO) {
        log.info("requested-> [POST]-'/api/events/saved'");
        EventDTO savedEvent = eventService.save(eventDTO);
        log.info("SAVED {} EVENT SUCCESSFULLY", eventDTO);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody EventDTO eventDTO, @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/events/update/{id}'");
        eventDTO.setId(id);
        eventService.update(eventDTO);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/events/{id}/deleted'");
        eventService.deleteById(id);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/events/delete/all'");
        eventService.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public EventResponse findEvents(@RequestBody EventExtraDTO eventExtraDTO) {
        log.info("requested-> [GET]->'/api/events/find'");
        List<EventDTO> eventsDTO = eventService.find(eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", eventsDTO.size(), eventExtraDTO.getStartDateTime(), eventExtraDTO.getEndDateTime());
        return new EventResponse(eventsDTO);
    }

}
