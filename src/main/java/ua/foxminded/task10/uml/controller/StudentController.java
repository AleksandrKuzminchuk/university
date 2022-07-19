package ua.foxminded.task10.uml.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.model.Course;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Person;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    @GetMapping
    public String findAll(Model model) {
        log.info("requested-> [GET]-'/students'");
        List<StudentDTO> students = studentService.findAll();
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        log.info("FOUND {} STUDENTS", students.size());
        return "students/students";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newStudent") StudentDTO studentDTO) {
        log.info("requested-> [GET]-'/students/new_student'");
        return "students/formSaveStudent";
    }

    @PostMapping("/saved")
    public String save(Model model,
                       @ModelAttribute @Valid StudentDTO studentDTO,
                       BindingResult bindingResult) {
        log.info("requested-> [POST]-'/savedStudent'");
        if (bindingResult.hasErrors()) {
            return "students/formSaveStudent";
        }
        StudentDTO newStudent = studentService.save(studentDTO);
        model.addAttribute("student", newStudent);
        log.info("SAVED {} SUCCESSFULLY", newStudent);
        return "students/formSavedStudent";
    }

    @GetMapping("/{id}/update")
    public String updateForm(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/students/{studentId}/update'");
        StudentDTO studentDTO = studentService.findById(id);
        List<GroupDTO> groups = groupService.findAll();
        model.addAttribute("student", studentDTO);
        model.addAttribute("groups", groups);
        log.info("UPDATING... {}", studentDTO);
        return "students/formUpdateStudent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model,
                         @ModelAttribute @Valid StudentDTO studentDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/students/{id}/updated'");
        if (bindingResult.hasErrors()) {
            return "students/formUpdateStudent";
        }
        studentDTO.setId(id);
        studentService.update(studentDTO);
        model.addAttribute("studentUpdated", studentDTO);
        log.info("UPDATED {} SUCCESSFULLY", studentDTO);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model,
                             @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/students/{id}/delete_student'");
        StudentDTO studentDTO = studentService.findById(id);
        studentService.deleteById(id);
        model.addAttribute("deleteStudentById", studentDTO);
        log.info("DELETED STUDENT BY ID - {}", id);
        return "students/formDeletedStudent";
    }

    @PatchMapping("/{id}/delete/from_group")
    public String deleteFromGroup(Model model,
                                  @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/students/{id}/delete/from_group'");
        StudentDTO studentDTO = studentService.deleteGroup(id);
        model.addAttribute("student", studentDTO);
        model.addAttribute("group", new Group());
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", id);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("/delete/all/by_group/{groupId}")
    public String deleteAllByGroupId(Model model,
                                     @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [DELETE]-'/students/delete/all/by_group/{groupId}'");
        Long countStudentsByGroupId = studentService.countByGroupId(groupId);
        studentService.deleteByGroupId(groupId);
        model.addAttribute("count", countStudentsByGroupId);
        log.info("DELETED {} BY GROUP ID - {} STUDENTS SUCCESSFULLY", countStudentsByGroupId, groupId);
        return "students/deletedAllStudentsByGroupId";
    }

    @GetMapping("/find/by_course")
    public String findByCourseNumberForm(@ModelAttribute("course") Course course) {
        log.info("requested-> [GET]-'/students/find/by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("/found/by_course")
    public String findByCourseNumber(Model model,
                                     @ModelAttribute @Valid Course course, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/students/found/by_course'");
        if (bindingResult.hasErrors()) {
            return "students/formFindStudentsByCourse";
        }
        List<StudentDTO> students = studentService.findByCourseNumber(course.getCourse());
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        model.addAttribute("courseNumber", course.getCourse());
        log.info("FOUND STUDENTS {} BY COURSE NUMBER {}", students.size(), course.getCourse());
        return "students/students";
    }

    @DeleteMapping("/delete/by_course/{course}")
    public String deleteAllByCourseNumber(Model model,
                                          @PathVariable("course") Integer course) {
        log.info("requested-> [DELETE]-'/students/delete/by_course/{course}'");
        studentService.deleteByCourseNumber(course);
        model.addAttribute("courseNumber", course);
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", course);
        return "students/formDeletedStudentsByCourseNumber";
    }

    @GetMapping("/find/by_group")
    public String formForFindByGroupName(Model model,
                                         @ModelAttribute("student") StudentDTO studentDTO) {
        log.info("requested-> [GET]-'/students/find/by_group'");
        model.addAttribute("groups", groupService.findAll());
        return "students/formForFindStudentsByGroupName";
    }

    @GetMapping("/found/by_group")
    public String findByGroupName(Model model,
                                  @ModelAttribute StudentDTO studentDTO) {
        log.info("requested-> [GET]-'/students/found/by_group'");
        List<StudentDTO> result = studentService.findByGroupName(studentDTO.getGroup().getName());
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("groupId", studentDTO.getGroup().getId());
        model.addAttribute("group", studentDTO.getGroup());
        log.info("FOUND STUDENTS {} BY GROUP ID {}", result.size(), studentDTO.getGroup().getId());
        return "students/students";
    }

    @GetMapping("/found/by_group/{groupId}")
    public String findByGroupId(Model model,
                                @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [GET]-'/students/found/by_group/{groupId}'");
        List<StudentDTO> result = studentService.findByGroupId(groupId);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("group", groupId);
        log.info("FOUND STUDENT {} BY GROUP ID {}", result.size(), groupId);
        return "students/students";
    }

    @GetMapping("/find/by_name_surname")
    public String formForFindByNameOrSurname(@ModelAttribute("newStudent") Person student) {
        log.info("requested-> [GET]-'/students/find/by_name_surname'");
        return "students/formFindStudentByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findByNameOrSurname(Model model, @ModelAttribute Person student) {
        log.info("requested-> [GET]-'/students/found/by_name_surname'");
        List<StudentDTO> result = studentService.findByNameOrSurname(student.getFirstName(), student.getLastName());
        model.addAttribute("students", result);
        log.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result);
        return "students/students";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAll(Model model) {
        log.info("requested- [DELETE]-'/students/delete/all'");
        Long countStudents = studentService.count();
        studentService.deleteAll();
        model.addAttribute("count", countStudents);
        log.info("DELETED ALL STUDENT SUCCESSFULLY");
        return "students/formDeleteAllStudents";
    }
}
