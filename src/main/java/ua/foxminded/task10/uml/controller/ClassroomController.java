package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.List;

@Controller
@RequestMapping("/classrooms")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_= @Autowired)
public class ClassroomController {

    ClassroomService classroomService;

    @GetMapping()
    public String findAllClassrooms(Model model){
        log.info("requested-> [GET]-'/classrooms'");
        List<Classroom> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        log.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("/new")
    public String createFormForSaveClassroom(@ModelAttribute("newClassroom") Classroom classroom){
        log.info("requested-> [GET]-'/new'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("/saved")
    public String saveClassroom(Model model, @ModelAttribute Classroom classroom){
        log.info("requested-> [POST]-'/saved'");
        Classroom newClassroom = classroomService.save(classroom);
        model.addAttribute("newClassroom", newClassroom);
        log.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("/{classroomId}/update")
    public String createFormForUpdateClassroom(Model model, @PathVariable("classroomId") Integer classroomId){
        log.info("requested-> [GET]-'/{classroomId}/update'");
        Classroom classroom = classroomService.findById(classroomId);
        model.addAttribute("classroom", classroom);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("/{classroomId}/updated")
    public String updateClassroom(Model model, @ModelAttribute Classroom classroom,
                                  @PathVariable("classroomId") Integer classroomId){
        log.info("requested-> [PATCH]-'/{classroomId}/updated'");
        classroomService.updateClassroom(classroomId, classroom);
        model.addAttribute("updatedClassroom", classroom);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", classroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("/{classroomId}/deleted")
    public String deleteClassroomById(Model model, @PathVariable("classroomId") Integer classroomId){
        log.info("requested-> [DELETE]-'/{classroomId}/deleted'");
        Classroom classroom = classroomService.findById(classroomId);
        classroomService.deleteById(classroomId);
        model.addAttribute("deleteClassroom", classroom);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("/find/by_number")
    public String createFormFindClassroomByNumber(@ModelAttribute("classroom") Classroom classroom){
        log.info("requested-> [GET]-'/find/by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("/found/by_number")
    public String findClassroomByNumber(Model model, @ModelAttribute Classroom classroom){
        log.info("requested-> [GET]-'/found/by_number'");
        List<Classroom> result = classroomService.findClassroomsByNumber(classroom.getNumber());
        model.addAttribute("classrooms", result);
        model.addAttribute("count", result.size());
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result.size(), classroom.getNumber());
        return "classrooms/classrooms";
    }

    @DeleteMapping("/deleted/all")
    public String deletedAllClassrooms(Model model){
        log.info("requested- [DELETE]-'/deleted/all'");
        Long countClassrooms = classroomService.count();
        classroomService.deleteAll();
        model.addAttribute("classrooms", countClassrooms);
        log.info("DELETED ALL {} CLASSROOMS SUCCESSFULLY", countClassrooms);
        return "classrooms/formDeletedAllClassrooms";
    }

}
