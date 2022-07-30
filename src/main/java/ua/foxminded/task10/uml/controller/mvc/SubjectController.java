package ua.foxminded.task10.uml.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.SubjectAddTeacherResponse;
import ua.foxminded.task10.uml.dto.response.SubjectFindTeachersResponse;
import ua.foxminded.task10.uml.dto.response.SubjectUpdateTeacherResponse;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;
import ua.foxminded.task10.uml.util.validations.SubjectValidator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectValidator subjectValidator;

    @GetMapping
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/subjects'");
        List<SubjectDTO> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", subjects.size());
        return "subjects/subjects";
    }

    @GetMapping("/subjects/new")
    public String saveForm(@ModelAttribute("newSubject") SubjectDTO subjectDTO) {
        log.info("requested-> [GET]-'/new");
        return "subjects/formForSaveSubject";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute("newSubject") @Valid @NotNull SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/subjects/saved'");
        subjectValidator.validate(subjectDTO, bindingResult);
        if (bindingResult.hasErrors()){
            return "subjects/formForSaveSubject";
        }
        SubjectDTO newSubjectDTO = subjectService.save(subjectDTO);
        model.addAttribute("subject", newSubjectDTO);
        log.info("SAVED {} SUCCESSFULLY", newSubjectDTO);
        return "subjects/formSavedSubject";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/subjects/{id}/update'");
        SubjectDTO subjectDTO = subjectService.findById(id);
        model.addAttribute("subject", subjectDTO);
        log.info("UPDATING... {}", subjectDTO);
        return "subjects/formForUpdateSubject";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @ModelAttribute @Valid @NotNull SubjectDTO subjectDTO, BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/subjects/{id}/updated'");
        subjectValidator.validate(subjectDTO, bindingResult);
        if (bindingResult.hasErrors()){
            return "subjects/formForUpdateSubject";
        }
        subjectDTO.setId(id);
        subjectService.update(subjectDTO);
        model.addAttribute("subjectUpdated", subjectDTO);
        log.info("UPDATED {} SUCCESSFULLY", subjectDTO);
        return "subjects/formUpdatedSubject";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/subjects/{id}/deleted'");
        subjectService.deleteById(id);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
        return "subjects/formDeletedSubject";
    }

    @DeleteMapping("/delete/all")
    public String deleteAll() {
        log.info("requested-> [DELETE]-'/subjects/delete/all'");
        subjectService.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return "subjects/formDeleteAllSubjects";
    }

    @GetMapping("/find/by_name")
    public String findByNameForm(@ModelAttribute("subject") SubjectDTO subjectDTO) {
        log.info("requested-> [GET]-'/subjects/find/by_name'");
        return "subjects/formForFindSubjectByName";
    }

    @GetMapping("/found/by_name")
    public String findByName(Model model, @ModelAttribute @Valid @NotNull SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/subjects/found/by_name'");
        if (bindingResult.hasErrors()){
            return "subjects/formForFindSubjectByName";
        }
        SubjectDTO result = subjectService.findByName(subjectDTO.getName());
        model.addAttribute("subjects", result);
        log.info("FOUND {} SUBJECT BY NAME {}", result, subjectDTO.getName());
        return "subjects/subjects";
    }

    @GetMapping("/{id}/add/teacher")
    public String addTeacherForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/subjects/{id}/add/teacher'");
        SubjectAddTeacherResponse addTeacher = subjectService.addTeacherForm(id);
        model.addAttribute("addTeacher", addTeacher);
        model.addAttribute("teacher", new TeacherDTO());
        log.info("PREPARED FORM ADD TEACHER TO SUBJECT BY ID - {}", id);
        return "subjects/formForAddSubjectToTeacher";
    }

    @PostMapping("/{id}/added/teacher")
    public String addTeacher(@ModelAttribute TeacherDTO teacherDTO,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/subjects/{id}/added/teacher'");
        subjectService.addTeacher(id, teacherDTO.getId());
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", id, teacherDTO.getId());
        return "subjects/formAddedSubjectToTeacher";
    }

    @GetMapping("/{id}/show/teachers")
    public String findTeachers(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/subjects/{id}/show/teachers'");
        SubjectFindTeachersResponse findTeachers = subjectService.findTeachersForm(id);
        model.addAttribute("teachers", findTeachers.getTeachers());
        model.addAttribute("subject", findTeachers.getSubject());
        model.addAttribute("count", findTeachers.getTeachers().size());
        log.info("FOUND {} TEACHERS TO SUBJECT BY ID - {} SUCCESSFULLY", findTeachers.getTeachers().size(), id);
        return "subjects/formFindTeachersToSubject";
    }

    @GetMapping("/{subjectId}/update/{teacherId}/teacher")
    public String updateTeacherForm(Model model,
                                    @PathVariable("subjectId") Integer subjectId,
                                    @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [GET]-'/subjects/{subjectId}/update/{teacherId}/teacher'");
        SubjectUpdateTeacherResponse updateTeacher = subjectService.updateTeacherForm(subjectId, teacherId);
        model.addAttribute("newTeacher", new TeacherDTO());
        model.addAttribute("updateTeacher", updateTeacher);
        log.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        return "subjects/formUpdateTheSubjectTeacher";
    }

    @PatchMapping("/{subjectId}/updated/{oldTeacherId}/teacher")
    public String updateTeacher(Model model, @ModelAttribute TeacherDTO teacherDTO,
                                          @PathVariable("oldTeacherId") Integer oldTeacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/subjects/{subjectId}/updated/{oldTeacherId}/teacher'");
        subjectService.updateTeacher(subjectId, oldTeacherId, teacherDTO.getId());
        model.addAttribute("subject", subjectId);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, teacherDTO.getId());
        return "subjects/formUpdatedTheSubjectTeacher";
    }

    @DeleteMapping("/{subjectId}/deleted/{teacherId}/teacher")
    public String deleteTeacher(Model model, @PathVariable("subjectId") Integer subjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/subjects/{subjectId}/deleted/{teacherId}/teacher'");
        subjectService.deleteTeacher(subjectId, teacherId);
        model.addAttribute("subject", subjectId);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return "subjects/formDeleteTheSubjectTeacher";
    }
}
