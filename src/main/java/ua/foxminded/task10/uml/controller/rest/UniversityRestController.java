package ua.foxminded.task10.uml.controller.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.dto.response.EventResponse;
import ua.foxminded.task10.uml.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/university")
@Api(value = "university-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"University API"})
public class UniversityRestController {

    private final EventService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find events",
            notes = "Finding events for present day",
            nickname = "showUniversity",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = EventResponse.class,
            responseContainer = "EventResponse")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Found events for present day successfully",
            response = EventResponse.class,
            responseContainer = "EventResponse")})
    public EventResponse showUniversity() {
        log.info("University home page -> [GET]-'/api/university'");
        List<EventDTO> eventList = service.find(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        return new EventResponse(eventList);
    }
}
