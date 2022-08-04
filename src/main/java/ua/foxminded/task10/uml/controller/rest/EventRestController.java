package ua.foxminded.task10.uml.controller.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.EventCreateDTO;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.EventExtraDTO;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.mapper.EventMapper;
import ua.foxminded.task10.uml.dto.response.EventResponse;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.service.*;
import ua.foxminded.task10.uml.util.errors.ErrorResponse;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Api(value = "event-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Event API"})
public class EventRestController {

    private final EventService service;
    private final EventMapper mapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all events",
            notes = "Finding all events from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = EventResponse.class,
            httpMethod = "GET",
            responseContainer = "EventResponse")
    @ApiResponses(value = @ApiResponse(
            code = 200,
            message = "Found all events successfully",
            responseContainer = "EventResponse",
            response = EventResponse.class))
    public EventResponse findAll() {
        log.info("requested-> [GET]-'/api/events'");
        List<EventDTO> eventsDTO = service.findAll();
        return new EventResponse(eventsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save event",
            notes = "Saving event to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = EventDTO.class,
            httpMethod = "POST",
            responseContainer = "EventDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "The event created successfully",
                    response = EventDTO.class,
                    responseContainer = "EventDTO"),
            @ApiResponse(
                    code = 404,
                    message = "Classroom, Subject, Teacher, Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public EventDTO save(@ApiParam(value = "EventCreateDTO instance") @RequestBody EventCreateDTO eventCreateDTO) {
        log.info("requested-> [POST]-'/api/events/saved'");
        EventDTO saveEventDTO = mapper.map(eventCreateDTO);
        EventDTO savedEvent = service.save(saveEventDTO);
        log.info("SAVED {} EVENT SUCCESSFULLY", savedEvent);
        return savedEvent;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update event",
            notes = "Updating event from DB to new Event",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = EventDTO.class,
            httpMethod = "PATCH",
            responseContainer = "EventDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The event updated successfully",
                    response = EventDTO.class,
                    responseContainer = "EventDTO"),
            @ApiResponse(
                    code = 404,
                    message = "Classroom, Subject, Teacher, Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public EventDTO update(@ApiParam(value = "Event Id") @PathVariable("id") Integer id,
                           @ApiParam(value = "EventCreateDTO instance") @RequestBody EventCreateDTO eventCreateDTO) {
        log.info("requested-> [PATCH]-'/api/events/update/{id}'");
        EventDTO updateEventDTO = mapper.map(eventCreateDTO);
        updateEventDTO.setId(id);
        service.update(updateEventDTO);
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", updateEventDTO);
        return updateEventDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete event",
            notes = "Deleting event from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Event deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Event not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteById(@ApiParam(value = "Event Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/events/{id}/deleted'");
        service.deleteById(id);
        log.info("DELETED EVENT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all events",
            notes = "Deleting all events from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all events successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/events/delete/all'");
        service.deleteAll();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find events",
            notes = "Finding events between startDateTime and endDateTime",
            nickname = "findEvents",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = EventResponse.class,
            responseContainer = "EventResponse")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Found events successfully",
            response = EventResponse.class,
            responseContainer = "EventResponse")})
    public EventResponse findEvents(@ApiParam(value = "startDateTime", defaultValue = "2020-08-02T11:11") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                    @RequestHeader(value = "startDateTime") LocalDateTime startDateTime,
                                    @ApiParam(value =  "endDateTime",defaultValue = "2023-08-02T11:11") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                    @RequestHeader(value = "endDateTime") LocalDateTime endDateTime) {
        log.info("requested-> [GET]->'/api/events/find'");
        List<EventDTO> eventsDTO = service.find(startDateTime, endDateTime);
        log.info("FOUND {} EVENTS BY PERIOD FROM {} TO {}", eventsDTO.size(), startDateTime, endDateTime);
        return new EventResponse(eventsDTO);
    }

}
