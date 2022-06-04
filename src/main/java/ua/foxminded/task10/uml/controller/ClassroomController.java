package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.List;

@Controller
@RequestMapping("/")
public class ClassroomController {
    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping("classrooms")
    @ResponseStatus(HttpStatus.OK)
    public String findAllClassrooms(Model model){
        logger.info("requested-> [GET]-'/classrooms'");
        List<Classroom> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        logger.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("new_classroom")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveClassroom(@ModelAttribute("newClassroom") Classroom classroom){
        logger.info("requested-> [GET]-'new_classroom'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("saved_classroom")
    @ResponseStatus(HttpStatus.OK)
    public String saveClassroom(Model model, @ModelAttribute Classroom classroom){
        logger.info("requested-> [POST]-'saved_classroom'");
        Classroom newClassroom = classroomService.save(classroom);
        model.addAttribute("newClassroom", newClassroom);
        logger.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("{classroomId}/update_classroom")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateClassroom(Model model, @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [GET]-'{classroomId}/update_classroom'");
        Classroom classroom = classroomService.findById(classroomId);
        model.addAttribute("classroom", classroom);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("{classroomId}/updated_classroom")
    @ResponseStatus(HttpStatus.OK)
    public String updateClassroom(Model model, @ModelAttribute Classroom classroom,
                                  @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [PATCH]-'{classroomId}/updated_classroom'");
        classroomService.updateClassroom(classroomId, classroom);
        model.addAttribute("updatedClassroom", classroom);
        logger.info("UPDATED {} CLASSROOM SUCCESSFULLY", classroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("{classroomId}/delete_classroom")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClassroomById(Model model, @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [DELETE]-'{classroomId}/delete_classroom'");
        Classroom classroom = classroomService.findById(classroomId);
        classroomService.deleteById(classroomId);
        model.addAttribute("deleteClassroom", classroom);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("find_classroom_by_number")
    @ResponseStatus(HttpStatus.OK)
    public String createFormFindClassroomByNumber(@ModelAttribute("classroom") Classroom classroom){
        logger.info("requested-> [GET]-'find_classroom_by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("found_classroom_by_number")
    @ResponseStatus(HttpStatus.OK)
    public String findClassroomByNumber(Model model, @ModelAttribute Classroom classroom){
        logger.info("requested-> [GET]-'found_classroom_by_number'");
        Classroom result = classroomService.findClassroomByNumber(classroom.getNumber());
        model.addAttribute("classrooms", result);
        logger.info("FOUND CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroom.getNumber());
        return "classrooms/foundClassroomByNumer";
    }

    @GetMapping("delete_classroom_by_number")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForDeleteClassroomByNumber(@ModelAttribute("classroom") Classroom classroom){
        logger.info("requested-> [DELETE]-'delete_classroom_by_number'");
        return "classrooms/formDeleteClassroomByNumber";
    }

    @DeleteMapping("deleted_classroom_by_number")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClassroomByNumber(Model model, @ModelAttribute Classroom classroom){
        logger.info("requested-> [DELETE]-'deleted_classroom_by_number'");
        classroomService.delete(classroom);
        model.addAttribute("classroom", classroom);
        logger.info("DELETED CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroom.getNumber());
        return "classrooms/formDeletedClassromByNumber";
    }

}
