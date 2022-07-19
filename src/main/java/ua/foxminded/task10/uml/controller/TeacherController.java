package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;

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
    public String save(Model model,
                       @ModelAttribute("newTeacher") @Valid TeacherDTO teacherDTO,
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
    public String deleteById(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/teachers/{id}/deleted'");
        TeacherDTO teacherDTO = teacherService.findById(id);
        teacherService.deleteById(id);
        model.addAttribute("deleteTeacherById", teacherDTO);
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
                         @ModelAttribute @Valid TeacherDTO teacherDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/teachers/{id}/updated'");
        if (bindingResult.hasErrors()) {
            return "teachers/formUpdateTeacher";
        }
        teacherDTO.setId(id);
        TeacherDTO updatedTeacher = teacherService.update(teacherDTO);
        model.addAttribute("updatedTeacher", updatedTeacher);
        log.info("UPDATED {} SUCCESSFULLY", updatedTeacher);
        return "teachers/formUpdatedTeacher";
    }

    @GetMapping("/{teacherId}/update/{subjectId}/subject")
    public String updateSubjectForm(Model model,
                                             @ModelAttribute("newSubject") Subject newSubject,
                                             @PathVariable("teacherId") Integer teacherId,
                                             @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'/teachers/{teacherId}/update/{subjectId}/subject'");
        SubjectDTO subjectDTO = subjectService.findById(subjectId);
        TeacherDTO teacherDTO = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("oldSubject", subjectDTO);
        model.addAttribute("subjects", subjectService.findAll());
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherDTO.getId(), subjectDTO.getId());
        return "teachers/formUpdateTheTeacherSubject";
    }

    @PatchMapping("/{teacherId}/updated/{oldSubjectId}/subject")
    public String updateSubject(Model model,
                                         @ModelAttribute Subject subject,
                                         @PathVariable("oldSubjectId") Integer oldSubjectId,
                                         @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'/teachers/{teacherId}/updated/{oldSubjectId}/subject'");
        SubjectDTO oldSubjectDTO = subjectService.findById(oldSubjectId);
        SubjectDTO newSubjectDTO = subjectService.findById(subject.getId());
        TeacherDTO teacherDTO = teacherService.findById(teacherId);
        teacherService.updateSubject(teacherId, oldSubjectId, newSubjectDTO.getId());
        model.addAttribute("newSubject", newSubjectDTO);
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("oldSubject", oldSubjectDTO);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectDTO.getId());
        return "teachers/formUpdatedTheTeacherSubject";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested-> [DELETE]-'/teachers/delete/all'");
        Long countTeachers = teacherService.count();
        teacherService.deleteAll();
        model.addAttribute("teachers", countTeachers);
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
    public String addSubjectForm(Model model,
                                          @ModelAttribute("subject") SubjectDTO subjectDTO,
                                          @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/teachers/{id}/add/subject'");
        TeacherDTO resultTeacherDTO = teacherService.findById(id);
        model.addAttribute("teacher", resultTeacherDTO);
        model.addAttribute("subjects", subjectService.findAll());
        log.info("FOUND TEACHER {} FOR ADD TO SUBJECT SUCCESSFULLY", resultTeacherDTO);
        return "teachers/formForAddTeacherToSubject";
    }

    @PostMapping("/{id}/added/subject")
    public String addSubject(Model model,
                                      @ModelAttribute SubjectDTO subjectDTO,
                                      @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/teachers/{id}/added/subject'");
        SubjectDTO resultSubjectDTO = subjectService.findById(subjectDTO.getId());
        TeacherDTO teacherDTO = teacherService.findById(id);
        teacherService.addSubject(teacherDTO.getId(), resultSubjectDTO.getId());
        model.addAttribute("addedTeacher", teacherDTO);
        model.addAttribute("subjectName", resultSubjectDTO);
        log.info("ADDED TEACHER {} TO SUBJECT {} SUCCESSFULLY", teacherDTO, resultSubjectDTO);
        return "teachers/formAddedTeacherToSubject";
    }

    @GetMapping("/{id}/show/subjects")
    public String findSubjects(Model model,
                                        @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/teachers/{id}/show/subjects'");
        TeacherDTO teacherDTO = teacherService.findById(id);
        List<SubjectDTO> subjectsDTO = teacherService.findSubjects(id);
        model.addAttribute("subjects", subjectsDTO);
        model.addAttribute("count", subjectsDTO.size());
        model.addAttribute("teacher", teacherDTO);
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjectsDTO.size(), id);
        return "teachers/formShowSubjectsByTeacherId";
    }

    @DeleteMapping("/{teacherId}/deleted/{subjectId}/subject")
    public String deleteSubject(Model model,
                                           @PathVariable("teacherId") Integer teacherId,
                                           @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/teachers/{teacherId}/deleted/{subjectId}/subject'");
        TeacherDTO teacherDTO = teacherService.findById(teacherId);
        SubjectDTO subjectDTO = subjectService.findById(subjectId);
        teacherService.deleteSubject(teacherId, subjectId);
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("subject", subjectDTO);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return "teachers/formDeleteTheTeacherSubject";
    }


}
