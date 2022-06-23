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
@RequestMapping("/subjects")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubjectController {

    SubjectService subjectService;
    TeacherService teacherService;

    @GetMapping()
    public String findAllSubjects(Model model) {
        log.info("requested-> [GET]-'/subjects'");
        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", subjects.size());
        return "subjects/subjects";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveSubject(@ModelAttribute("newSubject") Subject subject) {
        log.info("requested-> [GET]-'/new");
        return "subjects/formForSaveSubject";
    }

    @PostMapping("/saved")
    public String saveSubject(Model model, @ModelAttribute Subject subject) {
        log.info("requested-> [POST]-'/saved'");
        Subject newSubject = subjectService.save(subject);
        model.addAttribute("subject", newSubject);
        log.info("SAVED {} SUCCESSFULLY", newSubject);
        return "subjects/formSavedSubject";
    }

    @GetMapping("/{subjectId}/update")
    public String createFormForUpdateSubject(Model model, @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'/{subjectId}/update'");
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("subject", subject);
        log.info("UPDATING... {}", subject);
        return "subjects/formForUpdateSubject";
    }

    @PatchMapping("/{subjectId}/updated")
    public String updateSubject(Model model, @ModelAttribute Subject subject, @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/{subjectId}/updated'");
        subjectService.updateSubject(subjectId, subject);
        model.addAttribute("subjectUpdated", subject);
        log.info("UPDATED {} SUCCESSFULLY", subject);
        return "subjects/formUpdatedSubject";
    }

    @DeleteMapping("/{subjectId}/deleted")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSubjectById(Model model, @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/{subjectId}/deleted'");
        Subject subject = subjectService.findById(subjectId);
        subjectService.deleteById(subjectId);
        model.addAttribute("deleteSubjectById", subject);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", subjectId);
        return "subjects/formDeletedSubject";
    }

    @DeleteMapping("/delete/all")
    public String deleteAllSubjects(Model model) {
        log.info("requested-> [DELETE]-'/delete/all'");
        Long countSubjects = subjectService.count();
        subjectService.deleteAll();
        model.addAttribute("subjects", countSubjects);
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return "subjects/formDeleteAllSubjects";
    }

    @GetMapping("/find/by_name")
    public String createFormForFindSubjectByName(@ModelAttribute("subject") Subject subject) {
        log.info("requested-> [GET]-'/find/by_name'");
        return "subjects/formForFindSubjectByName";
    }

    @GetMapping("/found/by_name")
    public String findSubjectByName(Model model, @ModelAttribute Subject subject) {
        log.info("requested-> [GET]-'/found/by_name'");
        List<Subject> result = subjectService.findSubjectsByName(subject);
        model.addAttribute("subjects", result);
        model.addAttribute("count", result.size());
        log.info("FOUND {} SUBJECTS BY NAME {}", result.size(), subject.getName());
        return "subjects/subjects";
    }

    @GetMapping("/{subjectId}/add/teacher")
    public String createFormForAddSubjectToTeacher(Model model, @ModelAttribute("teacher") Teacher teacher,
                                                   @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'/{subjectId}/add/teacher'");
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("subject", subject);
        return "subjects/formForAddSubjectToTeacher";
    }

    @PostMapping("/{subjectId}/added/teacher")
    public String addSubjectToTeacher(Model model, @ModelAttribute Teacher teacher, @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [POST]-'/{subjectId}/added/teacher'");
        Teacher resultTeacher = teacherService.findById(teacher.getId());
        Subject subject = subjectService.findById(subjectId);
        subjectService.addSubjectToTeacher(subject, resultTeacher);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", resultTeacher);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacher.getId());
        return "subjects/formAddedSubjectToTeacher";
    }

    @GetMapping("/{subjectId}/show/teachers")
    public String findTeachersToSubject(Model model, @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [GET]-'/{subjectId}/show/teachers'");
        Subject subject = subjectService.findById(subjectId);
        List<Teacher> teachers = subjectService.findTeachersBySubject(subjectId);
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        model.addAttribute("subject", subject);
        log.info("FOUND {} TEACHERS TO SUBJECT BY ID - {} SUCCESSFULLY", teachers.size(), subjectId);
        return "subjects/formFindTeachersToSubject";
    }

    @GetMapping("/{subjectId}/update/{teacherId}/teacher")
    public String createFormChangeTheSubjectTeacher(Model model,
                                                    @ModelAttribute("newTeacher") Teacher newTeacher,
                                                    @PathVariable("subjectId") Integer subjectId,
                                                    @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [GET]-'/{subjectId}/update/{teacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("subject", subject);
        model.addAttribute("oldTeacher", teacher);
        model.addAttribute("teachers", teacherService.findAll());
        log.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subject.getId(), teacher.getId());
        return "subjects/formUpdateTheSubjectTeacher";
    }

    @PatchMapping("/{subjectId}/updated/{oldTeacherId}/teacher")
    public String updateTheSubjectTeacher(Model model, @ModelAttribute Teacher teacher,
                                          @PathVariable("oldTeacherId") Integer oldTeacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/{subjectId}/updated/{oldTeacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher oldTeacher = teacherService.findById(oldTeacherId);
        Teacher newTeacher = teacherService.findById(teacher.getId());
        subjectService.updateTheSubjectTeacher(subjectId, oldTeacherId, newTeacher.getId());
        model.addAttribute("newTeacher", newTeacher);
        model.addAttribute("oldTeacher", oldTeacher);
        model.addAttribute("subject", subject);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacher.getId());
        return "subjects/formUpdatedTheSubjectTeacher";
    }

    @DeleteMapping("/{subjectId}/deleted/{teacherId}/teacher")
    public String deleteTheSubjectTeacher(Model model, @PathVariable("subjectId") Integer subjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/{subjectId}/deleted/{teacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        subjectService.deleteTheSubjectTeacher(subjectId, teacherId);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", teacher);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return "subjects/formDeleteTheSubjectTeacher";
    }
}
