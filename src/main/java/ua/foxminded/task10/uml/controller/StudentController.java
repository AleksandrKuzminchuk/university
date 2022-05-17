package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.task10.uml.service.StudentService;

@Controller
@RequestMapping("/university")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/findAllStudents")
    public String findAll(Model model) {
        logger.info("/findAllStudents requested");
        model.addAttribute("students", studentService.findAll());
        return "students/allStudents";
    }

}
