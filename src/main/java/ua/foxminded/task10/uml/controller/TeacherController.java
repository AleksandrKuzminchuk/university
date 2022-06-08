package ua.foxminded.task10.uml.controller;

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
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;
    private final SubjectService subjectService;

    @Autowired
    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String findAllTeachers(Model model) {
        logger.info("requested-> [GET]-'/teachers'");
        List<Teacher> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        logger.info("FOUND {} TEACHERS SUCCESSFULLY", teachers.size());
        return "teachers/teachers";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveTeacher(@ModelAttribute("newTeacher") Teacher teacher) {
        logger.info("requested-> [GET]-'/new");
        return "teachers/formSaveTeacher";
    }

    @PostMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    public String saveTeacher(Model model, @ModelAttribute Teacher teacher) {
        logger.info("requested-> [POST]-'/saved'");
        Teacher newTeacher = teacherService.save(teacher);
        model.addAttribute("teacher", newTeacher);
        logger.info("SAVED {} SUCCESSFULLY", teacher);
        return "teachers/formSavedTeacher";
    }

    @DeleteMapping("{teacherId}/deleted")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTeacherById(Model model, @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [DELETE]-'{teacherId}/deleted'");
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.deleteById(teacherId);
        model.addAttribute("deleteTeacherById", teacher);
        logger.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
        return "teachers/formDeletedTeacher";
    }

    @GetMapping("{teacherId}/update")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateTeacher(Model model, @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [GET]-'{teacherId}/update'");
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacher);
        logger.info("UPDATING... {}", teacher);
        return "teachers/formUpdateTeacher";
    }

    @PatchMapping("{teacherId}/updated")
    @ResponseStatus(HttpStatus.OK)
    public String updateTeacher(Model model, @ModelAttribute Teacher teacher, @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [PATCH]-'{teacherId}/updated'");
        teacherService.updateTeacher(teacherId, teacher);
        model.addAttribute("updatedTeacher", teacher);
        logger.info("UPDATED {} SUCCESSFULLY", teacher);
        return "teachers/formUpdatedTeacher";
    }

    @GetMapping("{teacherId}/update/{subjectId}/subject")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForChangeTheTeacherSubject(Model model, @PathVariable("teacherId") Integer teacherId,
                                                       @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [GET]-'{teacherId}/update/{subjectId}/subject'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("oldSubject", subject);
        logger.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacher.getId(), subject.getId());
        return "teachers/formUpdateTheTeacherSubject";
    }

    @PatchMapping("{teacherId}/updated/{oldSubjectId}/subject")
    @ResponseStatus(HttpStatus.OK)
    public String updateTheTeacherSubject(Model model, @ModelAttribute Subject subject,
                                          @PathVariable("oldSubjectId") Integer oldSubjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [PATCH]-'{teacherId}/updated/{oldSubjectId}/subject'");
        Subject oldSubject = subjectService.findById(oldSubjectId);
        Subject newSubject = subjectService.findSubjectByName(subject);
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.updateTheTeacherSubject(teacherId, oldSubjectId, newSubject.getId());
        model.addAttribute("newSubject", newSubject);
        model.addAttribute("teacher", teacher);
        model.addAttribute("oldSubject", oldSubject);
        logger.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubject.getId());
        return "teachers/formUpdatedTheTeacherSubject";
    }

    @DeleteMapping("/deleted/all")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllTeachers(Model model) {
        logger.info("requested-> [DELETE]-'/delete/all'");
        List<Teacher> teachers = teacherService.findAll();
        teacherService.deleteAll();
        model.addAttribute("teachers", teachers.size());
        logger.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return "teachers/deleteAllTeachers";
    }

    @GetMapping("/find/by_name_surname")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindTeacherByNameSurname(@ModelAttribute("newTeacher") Teacher teacher) {
        logger.info("requested-> [GET]-'/find/by_name_surname'");
        return "teachers/formFindTeacherByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    @ResponseStatus(HttpStatus.OK)
    public String findTeacherByNameSurname(Model model, @ModelAttribute Teacher teacher) {
        logger.info("requested-> [GET]-'/found/by_name_surname'");
        Teacher result = teacherService.findTeacherByNameSurname(teacher);
        model.addAttribute("teachers", teacher);
        logger.info("FOUND TEACHER {} SUCCESSFULLY", result.getId());
        return "teachers/teachers";
    }

    @GetMapping("{teacherId}/add/subject")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForAddTeacherToSubject(Model model, @ModelAttribute("subject") Subject subject,
                                                   @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [GET]-'/{teacherId}/add/subject'");
        Teacher resultTeacher = teacherService.findById(teacherId);
        model.addAttribute("teacher", resultTeacher);
        logger.info("FOUND TEACHER {} FOR ADD TO SUBJECT SUCCESSFULLY", resultTeacher);
        return "teachers/formForAddTeacherToSubject";
    }

    @PostMapping("{teacherId}/added/subject")
    @ResponseStatus(HttpStatus.OK)
    public String assignTeacherToSubject(Model model, @ModelAttribute Subject subject, @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [POST]-'/{teacherId}/added/subject'");
        Subject resultSubject = subjectService.findSubjectByName(subject);
        Teacher teacher = teacherService.findById(teacherId);
        teacherService.addTeacherToSubject(teacher, resultSubject);
        model.addAttribute("addedTeacher", teacher);
        model.addAttribute("subjectName", resultSubject);
        logger.info("ASSIGNED TEACHER {} TO SUBJECT {} SUCCESSFULLY", teacher, resultSubject);
        return "teachers/formAddedTeacherToSubject";
    }

    @GetMapping("/{teacherId}/show/subjects")
    @ResponseStatus(HttpStatus.OK)
    public String findSubjectsByTeacherId(Model model, @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [GET]-'/{teacherId}/show/subjects'");
        Teacher teacher = teacherService.findById(teacherId);
        List<Subject> subjects = teacherService.findSubjectsByTeacherId(teacherId);
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        model.addAttribute("teacher", teacher);
        logger.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return "teachers/formShowSubjectsByTeacherId";
    }

    @DeleteMapping("{teacherId}/deleted/{subjectId}/subject")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTheTeacherSubject(Model model, @PathVariable("teacherId") Integer teacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [DELETE]-'{teacherId}/deleted/{subjectId}/subject'");
        Teacher teacher = teacherService.findById(teacherId);
        Subject subject = subjectService.findById(subjectId);
        teacherService.deleteTheTeacherSubject(teacherId, subjectId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("subject", subject);
        logger.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return "teachers/formDeleteTheTeacherSubject";
    }


}
