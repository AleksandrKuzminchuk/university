package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.service.ClassroomService;
import ua.foxminded.task10.uml.util.validations.ClassroomValidator;

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
        List<ClassroomDTO> classrooms = classroomService.findAll();
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("count", classrooms.size());
        log.info("FOUND {} CLASSROOMS SUCCESSFULLY", classrooms.size());
        return "classrooms/classrooms";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newClassroom") ClassroomDTO classroom) {
        log.info("requested-> [GET]-'/classrooms/new'");
        return "classrooms/formSaveClassroom";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute("newClassroom") @Valid ClassroomDTO classroomDTO,
                                BindingResult bindingResult) {
        log.info("requested-> [POST]-'/classrooms/saved'");
        classroomValidator.validate(classroomDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "classrooms/formSaveClassroom";
        }
        ClassroomDTO newClassroom = classroomService.save(classroomDTO);
        model.addAttribute("newClassroom", newClassroom);
        log.info("SAVED {} SUCCESSFULLY", newClassroom);
        return "classrooms/formSavedClassroom";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/classrooms/{id}/update'");
        ClassroomDTO classroomDTO = classroomService.findById(id);
        model.addAttribute("classroom", classroomDTO);
        return "classrooms/formUpdateClassroom";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @PathVariable("id") Integer id,
                         @ModelAttribute("classroom") @Valid ClassroomDTO classroomDTO, BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/classrooms/{id}/updated'");
        classroomValidator.validate(classroomDTO, bindingResult);
        if (bindingResult.hasFieldErrors("number")) {
            return "classrooms/formUpdateClassroom";
        }
        classroomDTO.setId(id);
        ClassroomDTO updatedClassroom = classroomService.update(classroomDTO);
        model.addAttribute("updatedClassroom", updatedClassroom);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", updatedClassroom);
        return "classrooms/formUpdatedClassroom";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/classrooms/{id}/deleted'");
        ClassroomDTO classroomDTO = classroomService.findById(id);
        classroomService.deleteById(id);
        model.addAttribute("deleteClassroom", classroomDTO);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", id);
        return "classrooms/formDeletedClassroom";
    }

    @GetMapping("/find/by_number")
    public String findByNumberForm(@ModelAttribute("classroom") ClassroomDTO classroomDTO) {
        log.info("requested-> [GET]-'/classrooms/find/by_number'");
        return "classrooms/formFindClassroomByNumber";
    }

    @GetMapping("/found/by_number")
    public String findByNumber(Model model, @ModelAttribute @Valid ClassroomDTO classroomDTO, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/classrooms/found/by_number'");
        if (bindingResult.hasErrors()) {
            return "classrooms/formFindClassroomByNumber";
        }
        ClassroomDTO result = classroomService.findByNumber(classroomDTO.getNumber());
        model.addAttribute("classrooms", result);
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result, classroomDTO.getNumber());
        return "classrooms/classrooms";
    }

    @DeleteMapping("/deleted/all")
    public String deletedAll(Model model) {
        log.info("requested- [DELETE]-'/classrooms/deleted/all'");
        Long countClassrooms = classroomService.count();
        classroomService.deleteAll();
        model.addAttribute("classrooms", countClassrooms);
        log.info("DELETED ALL {} CLASSROOMS SUCCESSFULLY", countClassrooms);
        return "classrooms/formDeletedAllClassrooms";
    }

}
