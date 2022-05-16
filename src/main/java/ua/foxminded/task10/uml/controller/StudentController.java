package ua.foxminded.task10.uml.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.task10.uml.service.StudentService;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/findAllStudents")
    public String findAll(Model model){
        model.addAttribute("students", studentService.findAll());
        return "students/allStudents";
    }


}
