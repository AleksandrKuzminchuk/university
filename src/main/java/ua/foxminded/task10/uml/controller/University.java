package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/university")
public class University {

    private static final Logger logger = LoggerFactory.getLogger(University.class);

    private final EventService eventService;

    public University(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String showUniversity(Model model) {
        logger.info("University home page");
        List<Event> eventList = eventService.findEvents(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        model.addAttribute("events", eventList);
        model.addAttribute("count", eventList.size());
        model.addAttribute("day", LocalDate.now());
        return "university";
    }
}
