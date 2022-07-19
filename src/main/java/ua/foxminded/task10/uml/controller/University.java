package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.task10.uml.dto.EventDTO;
import ua.foxminded.task10.uml.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/university")
public class University {
    private final EventService eventService;

    @GetMapping
    public String showUniversity(Model model) {
        log.info("University home page");
        List<EventDTO> eventList = eventService.find(LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        model.addAttribute("events", eventList);
        model.addAttribute("count", eventList.size());
        model.addAttribute("day", LocalDate.now());
        return "university";
    }
}
