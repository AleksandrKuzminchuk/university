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
@RequestMapping("/classrooms")
public class ClassroomController {
    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String findAllClassrooms(Model model){
        logger.info("requested-> [GET]-'/classrooms'");
        List<Classroom> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        logger.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveClassroom(@ModelAttribute("newClassroom") Classroom classroom){
        logger.info("requested-> [GET]-'/new'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    public String saveClassroom(Model model, @ModelAttribute Classroom classroom){
        logger.info("requested-> [POST]-'/saved'");
        Classroom newClassroom = classroomService.save(classroom);
        model.addAttribute("newClassroom", newClassroom);
        logger.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("/{classroomId}/update")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateClassroom(Model model, @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [GET]-'/{classroomId}/update'");
        Classroom classroom = classroomService.findById(classroomId);
        model.addAttribute("classroom", classroom);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("/{classroomId}/updated")
    @ResponseStatus(HttpStatus.OK)
    public String updateClassroom(Model model, @ModelAttribute Classroom classroom,
                                  @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [PATCH]-'/{classroomId}/updated'");
        classroomService.updateClassroom(classroomId, classroom);
        model.addAttribute("updatedClassroom", classroom);
        logger.info("UPDATED {} CLASSROOM SUCCESSFULLY", classroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("/{classroomId}/deleted")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClassroomById(Model model, @PathVariable("classroomId") Integer classroomId){
        logger.info("requested-> [DELETE]-'/{classroomId}/deleted'");
        Classroom classroom = classroomService.findById(classroomId);
        classroomService.deleteById(classroomId);
        model.addAttribute("deleteClassroom", classroom);
        logger.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("/find/by_number")
    @ResponseStatus(HttpStatus.OK)
    public String createFormFindClassroomByNumber(@ModelAttribute("classroom") Classroom classroom){
        logger.info("requested-> [GET]-'/find/by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("/found/by_number")
    @ResponseStatus(HttpStatus.OK)
    public String findClassroomByNumber(Model model, @ModelAttribute Classroom classroom){
        logger.info("requested-> [GET]-'/found/by_number'");
        Classroom result = classroomService.findClassroomByNumber(classroom.getNumber());
        model.addAttribute("classrooms", result);
        logger.info("FOUND CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroom.getNumber());
        return "classrooms/classrooms";
    }

    @DeleteMapping("/deleted/all")
    public String deletedAllClassrooms(Model model){
        logger.info("requested- [DELETE]-'/deleted/all'");
        List<Classroom> classrooms = classroomService.findAll();
        classroomService.deleteAll();
        model.addAttribute("classrooms", classrooms.size());
        logger.info("DELETED ALL {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/formDeletedAllClassrooms";
    }

}
