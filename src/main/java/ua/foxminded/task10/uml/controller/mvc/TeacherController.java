package ua.foxminded.task10.uml.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.TeacherAddSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherFindSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherUpdateSubjectResponse;
import ua.foxminded.task10.uml.service.TeacherService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/teachers'");
        List<TeacherDTO> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        log.info("FOUND {} TEACHERS SUCCESSFULLY", teachers.size());
        return "teachers/teachers";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newTeacher") TeacherDTO teacherDTO) {
        log.info("requested-> [GET]-'/teachers/new");
        return "teachers/formSaveTeacher";
    }

    @PostMapping("/saved")
    @ResponseStatus(HttpStatus.CREATED)
    public String save(Model model,
                       @ModelAttribute("newTeacher") @Valid @NotNull TeacherDTO teacherDTO,
                       BindingResult bindingResult) {
        log.info("requested-> [POST]-'/teachers/saved'");
        if (bindingResult.hasErrors()) {
            return "teachers/formSaveTeacher";
        }
        TeacherDTO newTeacherDTO = teacherService.save(teacherDTO);
        model.addAttribute("teacher", newTeacherDTO);
        log.info("SAVED {} SUCCESSFULLY", teacherDTO);
        return "teachers/formSavedTeacher";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/teachers/{id}/deleted'");
        teacherService.deleteById(id);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
        return "teachers/formDeletedTeacher";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/teachers/{id}/update'");
        TeacherDTO teacherDTO = teacherService.findById(id);
        model.addAttribute("teacher", teacherDTO);
        log.info("UPDATING... {}", teacherDTO);
        return "teachers/formUpdateTeacher";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model,
                         @ModelAttribute("updateTeacher") @Valid @NotNull TeacherDTO teacherDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/teachers/{id}/updated'");
        if (bindingResult.hasErrors()) {
            return "teachers/formUpdateTeacher";
        }
        teacherDTO.setId(id);
        teacherService.update(teacherDTO);
        model.addAttribute("updatedTeacher", teacherDTO);
        log.info("UPDATED {} SUCCESSFULLY", teacherDTO);
        return "teachers/formUpdatedTeacher";
    }

    @GetMapping("/{teacherId}/update/{subjectId}/subject")
    public String updateSubjectForm(Model model,
                                             @PathVariable("teacherId") Integer teacherId,
                                             @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'/teachers/{teacherId}/update/{subjectId}/subject'");
        TeacherUpdateSubjectResponse updateSubject = teacherService.updateSubjectForm(teacherId, subjectId);
        model.addAttribute("newSubject", new SubjectDTO());
        model.addAttribute("updateSubject", updateSubject);
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        return "teachers/formUpdateTheTeacherSubject";
    }

    @PatchMapping("/{teacherId}/updated/{oldSubjectId}/subject")
    public String updateSubject(Model model,
                                         @ModelAttribute("newSubject") SubjectDTO subjectDTO,
                                         @PathVariable("oldSubjectId") Integer oldSubjectId,
                                         @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'/teachers/{teacherId}/updated/{oldSubjectId}/subject'");
        teacherService.updateSubject(teacherId, oldSubjectId, subjectDTO.getId());
        model.addAttribute("teacher", teacherId);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, subjectDTO.getId());
        return "teachers/formUpdatedTheTeacherSubject";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll() {
        log.info("requested-> [DELETE]-'/teachers/deleted/all'");
        teacherService.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return "teachers/deleteAllTeachers";
    }

    @GetMapping("/find/by_name_surname")
    public String findByNameOrSurnameForm(@ModelAttribute("newTeacher") TeacherDTO teacherDTO) {
        log.info("requested-> [GET]-'/teachers/find/by_name_surname'");
        return "teachers/formFindTeacherByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findByNameOrSurname(Model model, @ModelAttribute("newTeacher") TeacherDTO teacherDTO) {
        log.info("requested-> [GET]-'/teachers/found/by_name_surname'");
        List<TeacherDTO> result = teacherService.findByNameOrSurname(teacherDTO.getFirstName(), teacherDTO.getLastName());
        model.addAttribute("teachers", result);
        model.addAttribute("count", result.size());
        log.info("FOUND {} TEACHERS BY {} SUCCESSFULLY", result.size(), teacherDTO);
        return "teachers/teachers";
    }

    @GetMapping("/{id}/add/subject")
    @ResponseStatus(HttpStatus.OK)
    public String addSubjectForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/teachers/{id}/add/subject'");
        TeacherAddSubjectResponse addSubject = teacherService.addSubjectFrom(id);
        model.addAttribute("addSubject", addSubject);
        model.addAttribute("subject", new SubjectDTO());
        log.info("PREPARED FORM ADD SUBJECT TO TEACHER BY ID- {}", id);
        return "teachers/formForAddTeacherToSubject";
    }

    @PostMapping("/{id}/added/subject")
    public String addSubject(@ModelAttribute("addSubject") SubjectDTO subjectDTO,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/teachers/{id}/added/subject'");
        teacherService.addSubject(id, subjectDTO.getId());
        log.info("ADDED TEACHER BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", id, subjectDTO.getId());
        return "teachers/formAddedTeacherToSubject";
    }

    @GetMapping("/{id}/show/subjects")
    public String findSubjects(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/teachers/{id}/show/subjects'");
        TeacherFindSubjectResponse findSubjects = teacherService.findSubjectsForm(id);
        model.addAttribute("subjects", findSubjects.getSubjects());
        model.addAttribute("count", findSubjects.getSubjects().size());
        model.addAttribute("teacher", findSubjects.getTeacher());
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", findSubjects.getSubjects(), id);
        return "teachers/formShowSubjectsByTeacherId";
    }

    @DeleteMapping("/{teacherId}/deleted/{subjectId}/subject")
    public String deleteSubject(Model model,
                                           @PathVariable("teacherId") Integer teacherId,
                                           @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/teachers/{teacherId}/deleted/{subjectId}/subject'");
        teacherService.deleteSubject(teacherId, subjectId);
        model.addAttribute("teacher", teacherId);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return "teachers/formDeleteTheTeacherSubject";
    }
}
