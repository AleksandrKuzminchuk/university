package ua.foxminded.task10.uml.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.response.StudentUpdateResponse;
import ua.foxminded.task10.uml.model.Course;
import ua.foxminded.task10.uml.model.Person;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.impl.StudentServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentServiceImpl studentService;
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
                       @ModelAttribute @Valid @NotNull StudentDTO studentDTO,
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
        log.info("requested-> [GET]-'/students/{id}/update'");
        StudentUpdateResponse updateResult = studentService.updateForm(id);
        model.addAttribute("studentUpdate", updateResult);
        log.info("UPDATED");
        return "students/formUpdateStudent";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model,
                         @ModelAttribute @Valid @NotNull StudentUpdateResponse updateResponse,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/students/{id}/updated'{}", updateResponse);
        if (bindingResult.hasErrors()) {
            return "students/formUpdateStudent";
        }
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(id);
        studentDTO.setFirstName(updateResponse.getStudent().getFirstName());
        studentDTO.setLastName(updateResponse.getStudent().getLastName());
        studentDTO.setGroup(updateResponse.getStudent().getGroup());
        studentDTO.setCourse(updateResponse.getStudent().getCourse());
        studentService.update(studentDTO);
        log.info("UPDATED {} SUCCESSFULLY", studentDTO);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/students/{id}/delete_student'");
        studentService.deleteById(id);
        log.info("DELETED STUDENT BY ID - {}", id);
        return "students/formDeletedStudent";
    }

    @PatchMapping("/{id}/delete/from_group")
    public String deleteFromGroup(@PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/students/{id}/delete/from_group'");
        studentService.deleteGroup(id);
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", id);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("/delete/all/by_group/{groupId}")
    public String deleteAllByGroupId(@PathVariable("groupId") Integer groupId) {
        log.info("requested-> [DELETE]-'/students/delete/all/by_group/{groupId}'");
        studentService.deleteByGroupId(groupId);
        log.info("DELETED STUDENTS BY GROUP ID - {} STUDENTS SUCCESSFULLY", groupId);
        return "students/deletedAllStudentsByGroupId";
    }

    @GetMapping("/find/by_course")
    public String findByCourseNumberForm(@ModelAttribute("course") Course course) {
        log.info("requested-> [GET]-'/students/find/by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("/found/by_course")
    public String findByCourseNumber(Model model,
                                     @ModelAttribute @Valid @NotNull Course course, BindingResult bindingResult) {
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
    public String findByGroupNameForm(Model model,
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
    public String deleteAll() {
        log.info("requested- [DELETE]-'/students/delete/all'");
        studentService.deleteAll();
        log.info("DELETED ALL STUDENT SUCCESSFULLY");
        return "students/formDeleteAllStudents";
    }
}
