package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.List;

@Controller
@RequestMapping("/teachers")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TeacherController {

    TeacherService teacherService;
    SubjectService subjectService;

    @GetMapping()
    public String findAllTeachers(Model model) {
        log.info("requested-> [GET]-'/teachers'");
        List<Teacher> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        log.info("FOUND {} TEACHERS SUCCESSFULLY", teachers.size());
        return "teachers/teachers";
    }

    @GetMapping("/new")
    public String createFormForSaveTeacher(@ModelAttribute("newTeacher") Teacher teacher) {
        log.info("requested-> [GET]-'/new");
        return "teachers/formSaveTeacher";
    }

    @PostMapping("/saved")
    public String saveTeacher(Model model, @ModelAttribute Teacher teacher) {
        log.info("requested-> [POST]-'/saved'");
        Teacher newTeacher = teacherService.save(teacher);
        model.addAttribute("teacher", newTeacher);
        log.info("SAVED {} SUCCESSFULLY", teacher);
        return "teachers/formSavedTeacher";
    }

    @DeleteMapping("{teacherId}/deleted")
    public String deleteTeacherById(Model model, @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'{teacherId}/deleted'");
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.deleteById(teacherId);
        model.addAttribute("deleteTeacherById", teacher);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
        return "teachers/formDeletedTeacher";
    }

    @GetMapping("{teacherId}/update")
    public String createFormForUpdateTeacher(Model model, @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [GET]-'{teacherId}/update'");
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacher);
        log.info("UPDATING... {}", teacher);
        return "teachers/formUpdateTeacher";
    }

    @PatchMapping("{teacherId}/updated")
    @ResponseStatus(HttpStatus.OK)
    public String updateTeacher(Model model, @ModelAttribute Teacher teacher, @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'{teacherId}/updated'");
        teacherService.updateTeacher(teacherId, teacher);
        model.addAttribute("updatedTeacher", teacher);
        log.info("UPDATED {} SUCCESSFULLY", teacher);
        return "teachers/formUpdatedTeacher";
    }

    @GetMapping("{teacherId}/update/{subjectId}/subject")
    public String createFormForChangeTheTeacherSubject(Model model,
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
    public String updateTheTeacherSubject(Model model, @ModelAttribute Subject subject,
                                          @PathVariable("oldSubjectId") Integer oldSubjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'{teacherId}/updated/{oldSubjectId}/subject'");
        Subject oldSubject = subjectService.findById(oldSubjectId);
        Subject newSubject = subjectService.findById(subject.getId());
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.updateTheTeacherSubject(teacherId, oldSubjectId, newSubject.getId());
        model.addAttribute("newSubject", newSubject);
        model.addAttribute("teacher", teacher);
        model.addAttribute("oldSubject", oldSubject);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubject.getId());
        return "teachers/formUpdatedTheTeacherSubject";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAllTeachers(Model model) {
        log.info("requested-> [DELETE]-'/delete/all'");
        Long countTeachers = teacherService.count();
        teacherService.deleteAll();
        model.addAttribute("teachers", countTeachers);
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return "teachers/deleteAllTeachers";
    }

    @GetMapping("/find/by_name_surname")
    public String createFormForFindTeacherByNameSurname(@ModelAttribute("newTeacher") Teacher teacher) {
        log.info("requested-> [GET]-'/find/by_name_surname'");
        return "teachers/formFindTeacherByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findTeachersByNameOrSurname(Model model, @ModelAttribute Teacher teacher) {
        log.info("requested-> [GET]-'/found/by_name_surname'");
        List<Teacher> result = teacherService.findTeachersByNameOrSurname(teacher);
        model.addAttribute("teachers", result);
        model.addAttribute("count", result.size());
        log.info("FOUND {} TEACHERS BY {} SUCCESSFULLY", result.size(), teacher);
        return "teachers/teachers";
    }

    @GetMapping("{teacherId}/add/subject")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForAddTeacherToSubject(Model model, @ModelAttribute("subject") Subject subject,
                                                   @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [GET]-'/{teacherId}/add/subject'");
        Teacher resultTeacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", resultTeacher);
        model.addAttribute("subjects", subjectService.findAll());
        log.info("FOUND TEACHER {} FOR ADD TO SUBJECT SUCCESSFULLY", resultTeacher);
        return "teachers/formForAddTeacherToSubject";
    }

    @PostMapping("{teacherId}/added/subject")
    public String assignTeacherToSubject(Model model, @ModelAttribute Subject subject, @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [POST]-'/{teacherId}/added/subject'");
        Subject resultSubject = subjectService.findById(subject.getId());
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.addTeacherToSubject(teacher, resultSubject);
        model.addAttribute("addedTeacher", teacher);
        model.addAttribute("subjectName", resultSubject);
        log.info("ASSIGNED TEACHER {} TO SUBJECT {} SUCCESSFULLY", teacher, resultSubject);
        return "teachers/formAddedTeacherToSubject";
    }

    @GetMapping("/{teacherId}/show/subjects")
    public String findSubjectsByTeacherId(Model model, @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [GET]-'/{teacherId}/show/subjects'");
        Teacher teacher = teacherService.findById(teacherId);
        List<Subject> subjects = teacherService.findSubjectsByTeacherId(teacherId);
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        model.addAttribute("teacher", teacher);
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return "teachers/formShowSubjectsByTeacherId";
    }

    @DeleteMapping("{teacherId}/deleted/{subjectId}/subject")
    public String deleteTheTeacherSubject(Model model, @PathVariable("teacherId") Integer teacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'{teacherId}/deleted/{subjectId}/subject'");
        Teacher teacher = teacherService.findById(teacherId);
        Subject subject = subjectService.findById(subjectId);
        teacherService.deleteTheTeacherSubject(teacherId, subjectId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("subject", subject);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return "teachers/formDeleteTheTeacherSubject";
    }


}
