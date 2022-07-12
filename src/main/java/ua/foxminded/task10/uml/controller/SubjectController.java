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
import ua.foxminded.task10.uml.util.SubjectValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final TeacherService teacherService;

    private final SubjectValidator subjectValidator;

    @GetMapping
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/subjects'");
        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", subjects.size());
        return "subjects/subjects";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newSubject") Subject subject) {
        log.info("requested-> [GET]-'/new");
        return "subjects/formForSaveSubject";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute("newSubject") @Valid Subject subject, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/saved'");
        subjectValidator.validate(subject, bindingResult);
        if (bindingResult.hasErrors()){
            return "subjects/formForSaveSubject";
        }
        Subject newSubject = subjectService.save(subject);
        model.addAttribute("subject", newSubject);
        log.info("SAVED {} SUCCESSFULLY", newSubject);
        return "subjects/formSavedSubject";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/{id}/update'");
        Subject subject = subjectService.findById(id);
        model.addAttribute("subject", subject);
        log.info("UPDATING... {}", subject);
        return "subjects/formForUpdateSubject";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @ModelAttribute @Valid Subject subject, BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/{id}/updated'");
        subjectValidator.validate(subject, bindingResult);
        if (bindingResult.hasErrors()){
            return "subjects/formForUpdateSubject";
        }
        subject.setId(id);
        subjectService.update(subject);
        model.addAttribute("subjectUpdated", subject);
        log.info("UPDATED {} SUCCESSFULLY", subject);
        return "subjects/formUpdatedSubject";
    }

    @DeleteMapping("/{id}/deleted")
    @ResponseStatus(HttpStatus.OK)
    public String deleteById(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/{id}/deleted'");
        Subject subject = subjectService.findById(id);
        subjectService.deleteById(id);
        model.addAttribute("deleteSubjectById", subject);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
        return "subjects/formDeletedSubject";
    }

    @DeleteMapping("/delete/all")
    public String deleteAll(Model model) {
        log.info("requested-> [DELETE]-'/delete/all'");
        Long countSubjects = subjectService.count();
        subjectService.deleteAll();
        model.addAttribute("subjects", countSubjects);
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return "subjects/formDeleteAllSubjects";
    }

    @GetMapping("/find/by_name")
    public String findByNameForm(@ModelAttribute("subject") Subject subject) {
        log.info("requested-> [GET]-'/find/by_name'");
        return "subjects/formForFindSubjectByName";
    }

    @GetMapping("/found/by_name")
    public String findByName(Model model, @ModelAttribute @Valid Subject subject, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/found/by_name'");
        if (bindingResult.hasErrors()){
            return "subjects/formForFindSubjectByName";
        }
        Subject result = subjectService.findByName(subject);
        model.addAttribute("subjects", result);
        log.info("FOUND {} SUBJECT BY NAME {}", result, subject.getName());
        return "subjects/subjects";
    }

    @GetMapping("/{id}/add/teacher")
    public String addTeacherForm(Model model, @ModelAttribute("teacher") Teacher teacher,
                                                   @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/{id}/add/teacher'");
        Subject subject = subjectService.findById(id);
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("subject", subject);
        return "subjects/formForAddSubjectToTeacher";
    }

    @PostMapping("/{id}/added/teacher")
    public String addTeacher(Model model, @ModelAttribute Teacher teacher, @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/{id}/added/teacher'");
        Teacher resultTeacher = teacherService.findById(teacher.getId());
        Subject subject = subjectService.findById(id);
        subjectService.addTeacher(subject, resultTeacher);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", resultTeacher);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", id, teacher.getId());
        return "subjects/formAddedSubjectToTeacher";
    }

    @GetMapping("/{id}/show/teachers")
    public String findTeachers(Model model, @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/{id}/show/teachers'");
        Subject subject = subjectService.findById(id);
        List<Teacher> teachers = subjectService.findTeachers(id);
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        model.addAttribute("subject", subject);
        log.info("FOUND {} TEACHERS TO SUBJECT BY ID - {} SUCCESSFULLY", teachers.size(), id);
        return "subjects/formFindTeachersToSubject";
    }

    @GetMapping("/{subjectId}/update/{teacherId}/teacher")
    public String updateTeacherForm(Model model,
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
    public String updateTeacher(Model model, @ModelAttribute Teacher teacher,
                                          @PathVariable("oldTeacherId") Integer oldTeacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/{subjectId}/updated/{oldTeacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher oldTeacher = teacherService.findById(oldTeacherId);
        Teacher newTeacher = teacherService.findById(teacher.getId());
        subjectService.updateTeacher(subjectId, oldTeacherId, newTeacher.getId());
        model.addAttribute("newTeacher", newTeacher);
        model.addAttribute("oldTeacher", oldTeacher);
        model.addAttribute("subject", subject);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacher.getId());
        return "subjects/formUpdatedTheSubjectTeacher";
    }

    @DeleteMapping("/{subjectId}/deleted/{teacherId}/teacher")
    public String deleteTeacher(Model model, @PathVariable("subjectId") Integer subjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/{subjectId}/deleted/{teacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        subjectService.deleteTeacher(subjectId, teacherId);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", teacher);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return "subjects/formDeleteTheSubjectTeacher";
    }
}
