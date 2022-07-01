package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@Controller
@RequiredArgsConstructor
@RequestMapping("/classrooms")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class ClassroomController {

    ClassroomService classroomService;
    ClassroomValidator classroomValidator;

    @GetMapping()
    public String findAllClassrooms(Model model) {
        log.info("requested-> [GET]-'/classrooms'");
        List<Classroom> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        log.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("/new")
    public String createFormForSaveClassroom(@ModelAttribute("newClassroom") Classroom classroom) {
        log.info("requested-> [GET]-'/new'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("/saved")
    public String saveClassroom(Model model, @Valid @ModelAttribute("newClassroom") Classroom classroom,
                                BindingResult bindingResult) {
        log.info("requested-> [POST]-'/saved'");
        classroomValidator.validate(classroom, bindingResult);
        if (bindingResult.hasErrors()) {
            return "classrooms/formSaveClassroom";
        }
        Classroom newClassroom = classroomService.save(classroom);
        model.addAttribute("newClassroom", newClassroom);
        log.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("/{classroomId}/update")
    public String createFormForUpdateClassroom(Model model, @PathVariable("classroomId") Integer classroomId) {
        log.info("requested-> [GET]-'/{classroomId}/update'");
        Classroom classroom = classroomService.findById(classroomId);
        model.addAttribute("classroom", classroom);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("/{classroomId}/updated")
    public String updateClassroom(Model model, @ModelAttribute("classroom") @Valid Classroom classroom,
                                  BindingResult bindingResult,
                                  @PathVariable("classroomId") Integer classroomId) {
        log.info("requested-> [PATCH]-'/{classroomId}/updated'");
        classroomValidator.validate(classroom, bindingResult);
        if (bindingResult.hasErrors()) {
            return "classrooms/formUpdateClassroom";
        }
        classroomService.updateClassroom(classroomId, classroom);
        model.addAttribute("updatedClassroom", classroom);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", classroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("/{classroomId}/deleted")
    public String deleteClassroomById(Model model, @PathVariable("classroomId") Integer classroomId) {
        log.info("requested-> [DELETE]-'/{classroomId}/deleted'");
        Classroom classroom = classroomService.findById(classroomId);
        classroomService.deleteById(classroomId);
        model.addAttribute("deleteClassroom", classroom);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("/find/by_number")
    public String createFormFindClassroomByNumber(@ModelAttribute("classroom") Classroom classroom) {
        log.info("requested-> [GET]-'/find/by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("/found/by_number")
    public String findClassroomByNumber(Model model, @ModelAttribute @Valid Classroom classroom, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/found/by_number'");
        if (bindingResult.hasErrors()) {
            return "classrooms/formFindClassroomByNumber";
        }
        Classroom result = classroomService.findClassroomByNumber(classroom);
        model.addAttribute("classrooms", result);
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result, classroom.getNumber());
        return "classrooms/classrooms";
    }

    @DeleteMapping("/deleted/all")
    public String deletedAllClassrooms(Model model) {
        log.info("requested- [DELETE]-'/deleted/all'");
        Long countClassrooms = classroomService.count();
        classroomService.deleteAll();
        model.addAttribute("classrooms", countClassrooms);
        log.info("DELETED ALL {} CLASSROOMS SUCCESSFULLY", countClassrooms);
        return "classrooms/formDeletedAllClassrooms";
    }

}
