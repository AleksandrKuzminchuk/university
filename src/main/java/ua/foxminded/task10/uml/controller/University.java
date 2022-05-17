package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/university")
public class University {

    private static final Logger logger = LoggerFactory.getLogger(University.class);

    @GetMapping
    public String showUniversity() {
        logger.info("University home page");
        return "university/show_university";
    }
}
