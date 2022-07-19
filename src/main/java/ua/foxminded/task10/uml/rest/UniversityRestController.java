package ua.foxminded.task10.uml.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class UniversityRestController {

    private final EventService eventService;

    @GetMapping
    public EventResponse showUniversity() {
        log.info("University home page");
        List<EventDTO> eventList = eventService.find(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        return new EventResponse(eventList);
    }
}
