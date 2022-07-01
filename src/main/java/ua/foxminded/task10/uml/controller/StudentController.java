package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentController {

    StudentService studentService;
    GroupService groupService;

    @GetMapping
    public String findAllStudents(Model model) {
        log.info("requested-> [GET]-'/students'");
        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        log.info("FOUND {} STUDENTS", students.size());
        return "students/students";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newStudent") Student student) {
        log.info("requested-> [GET]-'/new_student'");
        return "students/formSaveStudent";
    }

    @PostMapping("/saved")
    public String save(Model model,
                       @ModelAttribute @Valid Student student,
                       BindingResult bindingResult) {
        log.info("requested-> [POST]-'/savedStudent'");
        if (bindingResult.hasErrors()) {
            return "students/formSaveStudent";
        }
        Student newStudent = studentService.save(student);
        model.addAttribute("student", newStudent);
        log.info("SAVED {} SUCCESSFULLY", newStudent);
        return "students/formSavedStudent";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/{studentId}/update'");
        Student student = studentService.findById(id);
        List<Group> groups = groupService.findAll();
        model.addAttribute("student", student);
        model.addAttribute("groups", groups);
        log.info("UPDATING... {}", student);
        return "students/formUpdateStudent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model,
                         @ModelAttribute @Valid Student student,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/{id}/updated'");
        if (bindingResult.hasErrors()) {
            return "students/formUpdateStudent";
        }
        student.setId(id);
        studentService.update(student);
        model.addAttribute("studentUpdated", student);
        log.info("UPDATED {} SUCCESSFULLY", student);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/{id}/delete_student'");
        Student student = studentService.findById(id);
        studentService.deleteById(id);
        model.addAttribute("deleteStudentById", student);
        log.info("DELETED STUDENT BY ID - {}", id);
        return "students/formDeletedStudent";
    }

    @PatchMapping("/{id}/delete/from_group")
    public String deleteFromGroup(Model model,
                                  @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'{id}/delete/from_group'");
        Student student = studentService.deleteFromGroupByStudentId(id);
        model.addAttribute("student", student);
        model.addAttribute("group", new Group());
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", id);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("/delete/all/by_group/{groupId}")
    public String deleteAllByGroupId(Model model,
                                     @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [DELETE]-'/delete/all/by_group/{groupId}'");
        Long countStudentsByGroupId = studentService.countByGroupId(groupId);
        studentService.deleteByGroupId(groupId);
        model.addAttribute("count", countStudentsByGroupId);
        log.info("DELETED {} BY GROUP ID - {} STUDENTS SUCCESSFULLY", countStudentsByGroupId, groupId);
        return "students/deletedAllStudentsByGroupId";
    }

    @GetMapping("/find/by_course")
    public String findByCourseNumber(@ModelAttribute("course") Integer courseNumber) { // FixMe!
        log.info("requested-> [GET]-'/find/by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("/found/by_course")
    public String findByCourseNumber(Model model,
                                     @ModelAttribute @Valid Student student,
                                     BindingResult bindingResult) {
        log.info("requested-> [GET]-'/found/by_course'");
        if (bindingResult.hasErrors()) {
            return "students/formFindStudentsByCourse";
        }
        List<Student> students = studentService.findByCourseNumber(student.getCourse());
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        model.addAttribute("courseNumber", student.getCourse());
        log.info("FOUND STUDENTS {} BY COURSE NUMBER {}", students.size(), student);
        return "students/students";
    }

    @DeleteMapping("/delete/by_course/{courseNumber}")
    public String deleteAllByCourseNumber(Model model,
                                          @PathVariable("courseNumber") Integer courseNumber) {
        log.info("requested-> [DELETE]-'/delete/by_course/{course}'");
        studentService.deleteByCourseNumber(courseNumber);
        model.addAttribute("courseNumber", courseNumber);
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", courseNumber);
        return "students/formDeletedStudentsByCourseNumber";
    }

    @GetMapping("/find/by_group")
    public String formForFindByGroupName(Model model,
                                         @ModelAttribute("student") Student student) {
        log.info("requested-> [GET]-'find/by_group'");
        model.addAttribute("groups", groupService.findAll());
        return "students/formForFindStudentsByGroupName";
    }

    @GetMapping("/found/by_group")
    public String findByGroupName(Model model,
                                  @ModelAttribute Student student) {
        log.info("requested-> [GET]-'found/by_group'");
        Objects.requireNonNull(student.getGroup(), "Group must be present!"); //FixMe!
        List<Student> result = studentService.findByGroupName(student.getGroup());
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("groupId", student.getGroup().getId());
        model.addAttribute("group", student.getGroup());
        log.info("FOUND STUDENTS {} BY GROUP ID {}", result.size(), student.getGroup().getId());
        return "students/students";
    }

    @GetMapping("/found/by_group/{groupId}")
    public String findByGroupId(Model model,
                                @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [GET]-'/found/by_group/{groupId}'");
        List<Student> result = studentService.findByGroupId(groupId);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("group", groupId);
        log.info("FOUND STUDENT {} BY GROUP ID {}", result.size(), groupId);
        return "students/students";
    }

    @GetMapping("/find/by_name_surname")
    public String formForFindByNameSurname(@ModelAttribute("newStudent") Student student) {
        log.info("requested-> [GET]-'find/by_name_surname'");
        return "students/formFindStudentByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findByNameSurname(Model model,
                                    @ModelAttribute @Valid Student student,
                                    BindingResult bindingResult) {
        log.info("requested-> [GET]-'found/by_name_surname'");
        if (bindingResult.hasErrors()) {
            return "students/formFindStudentByNameSurname";
        }
        List<Student> result = studentService.findByNameOrSurname(student);
        model.addAttribute("students", result);
        log.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result);
        return "students/students";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested- [DELETE]-'/delete/all'");
        Long countStudents = studentService.count();
        studentService.deleteAll();
        model.addAttribute("count", countStudents);
        log.info("DELETED ALL STUDENT SUCCESSFULLY");
        return "students/formDeleteAllStudents";
    }
}
