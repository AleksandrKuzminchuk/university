package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
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
        List<Teacher> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        log.info("FOUND {} TEACHERS SUCCESSFULLY", teachers.size());
        return "teachers/teachers";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newTeacher") Teacher teacher) {
        log.info("requested-> [GET]-'/new");
        return "teachers/formSaveTeacher";
    }

    @PostMapping("/saved")
    public String save(Model model,
                       @ModelAttribute("newTeacher") @Valid Teacher teacher,
                       BindingResult bindingResult) {
        log.info("requested-> [POST]-'/saved'");
        if (bindingResult.hasErrors()) {
            return "teachers/formSaveTeacher";
        }
        Teacher newTeacher = teacherService.save(teacher);
        model.addAttribute("teacher", newTeacher);
        log.info("SAVED {} SUCCESSFULLY", teacher);
        return "teachers/formSavedTeacher";
    }

    @DeleteMapping("{id}/deleted")
    public String deleteById(Model model,
                             @PathVariable("id") Integer teacherId) {
        log.info("requested-> [DELETE]-'{id}/deleted'");
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.deleteById(teacherId);
        model.addAttribute("deleteTeacherById", teacher);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
        return "teachers/formDeletedTeacher";
    }

    @GetMapping("{id}/update")
    public String updateForm(Model model,
                             @PathVariable("id") Integer teacherId) {
        log.info("requested-> [GET]-'{id}/update'");
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacher);
        log.info("UPDATING... {}", teacher);
        return "teachers/formUpdateTeacher";
    }

    @PatchMapping("{id}/updated")
    public String update(Model model,
                         @ModelAttribute @Valid Teacher teacher,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer teacherId) {
        log.info("requested-> [PATCH]-'{id}/updated'");
        if (bindingResult.hasErrors()) {
            return "teachers/formUpdateTeacher";
        }
        teacher.setId(teacherId);
        Teacher updatedTeacher = teacherService.update(teacher);
        model.addAttribute("updatedTeacher", updatedTeacher);
        log.info("UPDATED {} SUCCESSFULLY", updatedTeacher);
        return "teachers/formUpdatedTeacher";
    }

    @GetMapping("{teacherId}/update/{subjectId}/subject")
    public String updateAtTeacherSubjectForm(Model model,
                                             @ModelAttribute("newSubject") Subject newSubject,
                                             @PathVariable("teacherId") Integer teacherId,
                                             @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'{teacherId}/update/{subjectId}/subject'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("oldSubject", subject);
        model.addAttribute("subjects", subjectService.findAll());
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacher.getId(), subject.getId());
        return "teachers/formUpdateTheTeacherSubject";
    }

    @PatchMapping("{teacherId}/updated/{oldSubjectId}/subject")
    public String updateAtTeacherSubject(Model model,
                                         @ModelAttribute Subject subject,
                                         @PathVariable("oldSubjectId") Integer oldSubjectId,
                                         @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'{teacherId}/updated/{oldSubjectId}/subject'");
        Subject oldSubject = subjectService.findById(oldSubjectId);
        Subject newSubject = subjectService.findById(subject.getId());
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.updateAtTeacherSubject(teacherId, oldSubjectId, newSubject.getId());
        model.addAttribute("newSubject", newSubject);
        model.addAttribute("teacher", teacher);
        model.addAttribute("oldSubject", oldSubject);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubject.getId());
        return "teachers/formUpdatedTheTeacherSubject";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested-> [DELETE]-'/delete/all'");
        Long countTeachers = teacherService.count();
        teacherService.deleteAll();
        model.addAttribute("teachers", countTeachers);
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return "teachers/deleteAllTeachers";
    }

    @GetMapping("/find/by_name_surname")
    public String findTeachersByNameOrSurnameForm(@ModelAttribute("newTeacher") Teacher teacher) {//ToDo: remove 'Teacher(s)' from all method names!
        log.info("requested-> [GET]-'/find/by_name_surname'");
        return "teachers/formFindTeacherByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findTeachersByNameOrSurname(Model model,
                                              @ModelAttribute("newTeacher") Teacher teacher,
                                              BindingResult bindingResult) {
        log.info("requested-> [GET]-'/found/by_name_surname'");
        if (bindingResult.hasErrors()) {
            return "teachers/formFindTeacherByNameSurname";
        }
        List<Teacher> result = teacherService.findTeachersByNameOrSurname(teacher);
        model.addAttribute("teachers", result);
        model.addAttribute("count", result.size());
        log.info("FOUND {} TEACHERS BY {} SUCCESSFULLY", result.size(), teacher);
        return "teachers/teachers";
    }

    @GetMapping("{id}/add/subject")
    @ResponseStatus(HttpStatus.OK)
    public String addTeacherToSubjectForm(Model model,
                                          @ModelAttribute("subject") Subject subject,
                                          @PathVariable("id") Integer teacherId) {
        log.info("requested-> [GET]-'/{id}/add/subject'");
        Teacher resultTeacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", resultTeacher);
        model.addAttribute("subjects", subjectService.findAll());
        log.info("FOUND TEACHER {} FOR ADD TO SUBJECT SUCCESSFULLY", resultTeacher);
        return "teachers/formForAddTeacherToSubject";
    }

    @PostMapping("{id}/added/subject")
    public String addTeacherToSubject(Model model,
                                      @ModelAttribute Subject subject,
                                      @PathVariable("id") Integer teacherId) {
        log.info("requested-> [POST]-'/{id}/added/subject'");
        Subject resultSubject = subjectService.findById(subject.getId());
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.addTeacherToSubject(teacher, resultSubject);
        model.addAttribute("addedTeacher", teacher);
        model.addAttribute("subjectName", resultSubject);
        log.info("ADDED TEACHER {} TO SUBJECT {} SUCCESSFULLY", teacher, resultSubject);
        return "teachers/formAddedTeacherToSubject";
    }

    @GetMapping("/{id}/show/subjects")
    public String findSubjectsByTeacher(Model model,
                                        @PathVariable("id") Integer teacherId) {
        log.info("requested-> [GET]-'/{id}/show/subjects'");
        Teacher teacher = teacherService.findById(teacherId);
        List<Subject> subjects = teacherService.findSubjectsByTeacher(teacherId);
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        model.addAttribute("teacher", teacher);
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return "teachers/formShowSubjectsByTeacherId";
    }

    @DeleteMapping("{teacherId}/deleted/{subjectId}/subject")
    public String deleteFromTeacherSubject(Model model,
                                           @PathVariable("teacherId") Integer teacherId,
                                           @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'{teacherId}/deleted/{subjectId}/subject'");
        Teacher teacher = teacherService.findById(teacherId);
        Subject subject = subjectService.findById(subjectId);
        teacherService.deleteFromTeacherSubject(teacherId, subjectId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("subject", subject);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return "teachers/formDeleteTheTeacherSubject";
    }


}
