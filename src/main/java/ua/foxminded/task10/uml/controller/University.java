package ua.foxminded.task10.uml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/university")
public class University {

    public String showUniversity(){
        return "university/show_university";
    }
}
