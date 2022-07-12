package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;
import ua.foxminded.task10.uml.util.ClassroomValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomValidator classroomValidator;

    @GetMapping()
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/classrooms'");
        List<Classroom> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        log.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newClassroom") Classroom classroom) {
        log.info("requested-> [GET]-'/new'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute("newClassroom") @Valid Classroom classroom,
                                BindingResult bindingResult) {
        log.info("requested-> [POST]-'/saved'");
        classroomValidator.validate(classroom, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "classrooms/formSaveClassroom";
        }
        Classroom newClassroom = classroomService.save(classroom);
        model.addAttribute("newClassroom", newClassroom);
        log.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") Integer classroomId) {
        log.info("requested-> [GET]-'/{id}/update'");
        Classroom classroom = classroomService.findById(classroomId);
        model.addAttribute("classroom", classroom);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @PathVariable("id") Integer classroomId,
                         @ModelAttribute("classroom") @Valid Classroom classroom, BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/{id}/updated'");
        classroomValidator.validate(classroom, bindingResult);
        if (bindingResult.hasFieldErrors("number")) {
            return "classrooms/formUpdateClassroom";
        }
        classroom.setId(classroomId);
        Classroom updatedClassroom = classroomService.update(classroom);
        model.addAttribute("updatedClassroom", updatedClassroom);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", updatedClassroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model, @PathVariable("id") Integer classroomId) {
        log.info("requested-> [DELETE]-'/{id}/deleted'");
        Classroom classroom = classroomService.findById(classroomId);
        classroomService.deleteById(classroomId);
        model.addAttribute("deleteClassroom", classroom);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("/find/by_number")
    public String findByNumberForm(@ModelAttribute("classroom") Classroom classroom) {
        log.info("requested-> [GET]-'/find/by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("/found/by_number")
    public String findByNumber(Model model, @ModelAttribute @Valid Classroom classroom, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/found/by_number'");
        if (bindingResult.hasErrors()) {
            return "classrooms/formFindClassroomByNumber";
        }
        Classroom result = classroomService.findByNumber(classroom);
        model.addAttribute("classrooms", result);
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result, classroom.getNumber());
        return "classrooms/classrooms";
    }

    @DeleteMapping("/deleted/all")
    public String deletedAll(Model model) {
        log.info("requested- [DELETE]-'/deleted/all'");
        Long countClassrooms = classroomService.count();
        classroomService.deleteAll();
        model.addAttribute("classrooms", countClassrooms);
        log.info("DELETED ALL {} CLASSROOMS SUCCESSFULLY", countClassrooms);
        return "classrooms/formDeletedAllClassrooms";
    }

}
