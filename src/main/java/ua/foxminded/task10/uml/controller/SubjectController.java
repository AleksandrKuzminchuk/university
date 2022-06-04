package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/")
public class SubjectController {
    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final TeacherService teacherService;

    public SubjectController(SubjectService subjectService, TeacherService teacherService) {
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    @GetMapping("subjects")
    @ResponseStatus(HttpStatus.OK)
    public String findAllSubjects(Model model) {
        logger.info("requested-> [GET]-'/subjects'");
        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("count", subjects.size());
        logger.info("FOUND {} SUBJECTS SUCCESSFULLY", subjects.size());
        return "subjects/sudjects";
    }

    @GetMapping("new_subject")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveSubject(@ModelAttribute("newSubject") Subject subject) {
        logger.info("requested-> [GET]-'/new_subject");
        return "subjects/formForSaveSubject";
    }

    @PostMapping("saved_subject")
    @ResponseStatus(HttpStatus.OK)
    public String saveSubject(Model model, @ModelAttribute Subject subject) {
        logger.info("requested-> [POST]-'/saved_subject'");
        Subject newSubject = subjectService.save(subject);
        model.addAttribute("subject", newSubject);
        logger.info("SAVED {} SUCCESSFULLY", newSubject);
        return "subjects/formSavedSubject";
    }

    @GetMapping("{subjectId}/update_subject")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateSubject(Model model, @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [GET]-'{id}/update_subject'");
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("subject", subject);
        logger.info("UPDATING... {}", subject);
        return "subjects/formForUpdateSubject";
    }

    @PatchMapping("{subjectId}/updated_subject")
    @ResponseStatus(HttpStatus.OK)
    public String updateSubject(Model model, @ModelAttribute Subject subject, @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [PATCH]-'{id}/updated_subject'");
        subjectService.updateSubject(subjectId, subject);
        model.addAttribute("subjectUpdated", subject);
        logger.info("UPDATED {} SUCCESSFULLY", subject);
        return "subjects/formUpdatedSubject";
    }

    @DeleteMapping("{subjectId}/delete_subject")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSubjectById(Model model, @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [DELETE]-'{id}/delete_subject'");
        Subject subject = subjectService.findById(subjectId);
        subjectService.deleteById(subjectId);
        model.addAttribute("deleteSubjectById", subject);
        logger.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", subjectId);
        return "subjects/formDeletedSubject";
    }

    @DeleteMapping("delete_all_subjects")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllSubjects() {
        logger.info("requested-> [DELETE]-'delete_all_subjects'");
        subjectService.deleteAll();
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return "subjects/formDeleteAllSubjects";
    }

    @GetMapping("find_subject_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindSubjectByName(@ModelAttribute("subject") Subject subject) {
        logger.info("requested-> [GET]-'find_subject_by_name'");
        return "subjects/formForFindSubjectByName";
    }

    @GetMapping("found_subject_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String findSubjectByName(Model model, @ModelAttribute Subject subject) {
        logger.info("requested-> [GET]-'found_subject_by_name'");
        Subject result = subjectService.findSubjectByName(subject);
        model.addAttribute("subjects", result);
        logger.info("FOUND SUBJECT BY NAME {}", subject.getName());
        return "subjects/formFoundSubjectByName";
    }

    @GetMapping("delete_subject_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String createFormDeleteSubjectByName(@ModelAttribute("subject") Subject subject) {
        logger.info("requested-> [GET]-'delete_subject_by_name'");
        return "subjects/formForDeleteSubjectByName";
    }

    @DeleteMapping("deleted_subject_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSubjectByName(Model model, @ModelAttribute Subject subject) {
        logger.info("requested-> [DELETE]-'deleted_subject_by_name'");
        subjectService.delete(subject);
        model.addAttribute("subject", subject);
        logger.info("DELETED SUBJECT BY NAME {} SUCCESSFULLY", subject.getName());
        return "subjects/formDeletedSubjectByName";
    }

    @GetMapping("{subjectId}/add_subject_to_teacher")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForAddSubjectToTeacher(Model model, @ModelAttribute("teacher") Teacher teacher,
                                                   @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [GET]-'{id}/add_subject_to_teacher'");
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("subject", subject);
        return "subjects/formForAddSubjectToTeacher";
    }

    @PostMapping("{subjectId}/added_subject_to_teacher")
    @ResponseStatus(HttpStatus.OK)
    public String addSubjectToTeacher(Model model, @ModelAttribute Teacher teacher, @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [POST]-'{id}/added_subject_to_teacher'");
        Teacher resultTeacher = teacherService.findTeacherByNameSurname(teacher);
        Subject subject = subjectService.findById(subjectId);
        subjectService.addSubjectToTeacher(subject, resultTeacher);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", resultTeacher);
        logger.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacher.getId());
        return "subjects/formAddedSubjectToTeacher";
    }

    @GetMapping("{subjectId}/show_teachers_to_subject")
    @ResponseStatus(HttpStatus.OK)
    public String findTeachersToSubject(Model model, @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [GET]-'{id}/show_teachers_to_subject'");
        Subject subject = subjectService.findById(subjectId);
        List<Teacher> teachers = subjectService.findTeachersBySubject(subjectId);
        model.addAttribute("teachers", teachers);
        model.addAttribute("count", teachers.size());
        model.addAttribute("subject", subject);
        logger.info("FOUND {} TEACHERS TO SUBJECT BY ID - {} SUCCESSFULLY", teachers.size(), subjectId);
        return "subjects/formFindTeachersToSubject";
    }

    @GetMapping("{subjectId}/update_the_subject/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public String createFormChangeTheSubjectTeacher(Model model, @PathVariable("subjectId") Integer subjectId,
                                                    @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [GET]-'{subjectId}/update_the_subject/{teacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("subject", subject);
        model.addAttribute("oldTeacher", teacher);
        logger.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subject.getId(), teacher.getId());
        return "subjects/formUpdateTheSubjectTeacher";
    }

    @PatchMapping("{subjectId}/updated_the_subject/{oldTeacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public String updateTheSubjectTeacher(Model model, @ModelAttribute Teacher teacher,
                                          @PathVariable("oldTeacherId") Integer oldTeacherId,
                                          @PathVariable("subjectId") Integer subjectId) {
        logger.info("requested-> [PATCH]-'{subjectId}/updated_the_subject/{oldTeacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher oldTeacher = teacherService.findById(oldTeacherId);
        Teacher newTeacher = teacherService.findTeacherByNameSurname(teacher);
        subjectService.updateTheSubjectTeacher(subjectId, oldTeacherId, newTeacher.getId());
        model.addAttribute("newTeacher", newTeacher);
        model.addAttribute("oldTeacher", oldTeacher);
        model.addAttribute("subject", subject);
        logger.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacher.getId());
        return "subjects/formUpdatedTheSubjectTeacher";
    }

    @DeleteMapping("{subjectId}/delete_the_subject/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTheSubjectTeacher(Model model, @PathVariable("subjectId") Integer subjectId,
                                          @PathVariable("teacherId") Integer teacherId) {
        logger.info("requested-> [DELETE]-'{subjectId}/delete_the_subject/{teacherId}/teacher'");
        Subject subject = subjectService.findById(subjectId);
        Teacher teacher = teacherService.findById(teacherId);
        subjectService.deleteTheSubjectTeacher(subjectId, teacherId);
        model.addAttribute("subject", subject);
        model.addAttribute("teacher", teacher);
        logger.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return "subjects/formDeleteTheSubjectTeacher";
    }
}
