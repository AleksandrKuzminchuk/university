package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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
    public String createFormForSaveStudent(@ModelAttribute("newStudent") Student student) {
        log.info("requested-> [GET]-'/new_student'");
        return "students/formSaveStudent";
    }

    @PostMapping("/saved")
    public String saveStudent(Model model,
                              @ModelAttribute Student student) {
        log.info("requested-> [POST]-'/savedStudent'");
        Student newStudent = studentService.save(student);
        model.addAttribute("student", newStudent);
        log.info("SAVED {} SUCCESSFULLY", newStudent);
        return "students/formSavedStudent";
    }

    @GetMapping("/{studentId}/update")
    public String createFormForUpdateStudent(Model model,
                                             @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [GET]-'/{studentId}/update'");
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        log.info("UPDATING... {}", student);
        return "students/formUpdateStudent";
    }

    @PatchMapping("/{studentId}/updated")
    public String updateStudent(Model model,
                                @ModelAttribute Student student,
                                @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [PATCH]-'/{studentId}/updated'");
        studentService.updateStudent(studentId, student);
        model.addAttribute("studentUpdated", student);
        log.info("UPDATED {} SUCCESSFULLY", student);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("/{studentId}/deleted")
    public String deleteStudentById(Model model,
                                    @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [DELETE]-'/{studentId}/delete_student'");
        Student student = studentService.findById(studentId);
        studentService.deleteById(studentId);
        model.addAttribute("deleteStudentById", student);
        log.info("DELETED STUDENT BY ID - {}", studentId);
        return "students/formDeletedStudent";
    }

    @PatchMapping("/{studentId}/delete/from_group")
    public String deleteStudentFromGroup(Model model,
                                         @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [PATCH]-'{studentId}/delete/from_group'");
        Student student = studentService.findById(studentId);
        Group group = groupService.findById(student.getGroup().getId());
        studentService.deleteTheStudentGroup(studentId);
        model.addAttribute("student", student);
        model.addAttribute("group", group);
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", studentId);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("/delete/all/by_group/{groupId}")
    public String deleteAllStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [DELETE]-'/delete/all/by_group/{groupId}'");
        Long countStudentsByGroupId = studentService.countByGroupId(groupId);
        studentService.deleteStudentsByGroupId(groupId);
        model.addAttribute("count", countStudentsByGroupId);
        log.info("DELETED {} BY GROUP ID - {} STUDENTS SUCCESSFULLY", countStudentsByGroupId, groupId);
        return "students/deletedAllStudentsByGroupId";
    }

    @GetMapping("/find/by_course")
    public String createFormForFindStudentsByCourseNumber(@ModelAttribute("student") Student student) {
        log.info("requested-> [GET]-'/find/by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("/found/by_course")
    public String findStudentsByCourseNumber(Model model,
                                             @ModelAttribute Student studentCourse) {
        log.info("requested-> [GET]-'/found/by_course'");
        List<Student> students = studentService.findByCourseNumber(studentCourse.getCourse());
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        model.addAttribute("courseNumber", studentCourse.getCourse());
        log.info("FOUND STUDENTS {} BY COURSE NUMBER {}", students.size(), studentCourse);
        return "students/students";
    }

    @DeleteMapping("/delete/by_course/{course}")
    public String deleteStudentsByCourseNumber(Model model,
                                               @PathVariable("course") Integer studentCourse) {
        log.info("requested-> [DELETE]-'/delete/by_course/{course}'");
        studentService.deleteStudentsByCourseNumber(studentCourse);
        model.addAttribute("courseNumber", studentCourse);
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", studentCourse);
        return "students/formDeletedStudentsByCourseNumber";
    }

    @PatchMapping("/delete/by_group/{groupId}")
    public String deleteStudentsByGroupId(Model model,
                                          @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [PATCH]-'/delete/by_group/{groupId}'");
        Long countStudentsByGroupId = studentService.countByGroupId(groupId);
        Group group = groupService.findById(groupId);
        studentService.deleteStudentsByGroupId(groupId);
        model.addAttribute("groupName", group);
        model.addAttribute("count", countStudentsByGroupId);
        log.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
        return "students/formDeletedStudentsByGroupName";
    }

    @GetMapping("/{studentId}/update/group")
    public String createFormForChangeGroupTheStudent(Model model,
                                                     @ModelAttribute("group") Group group,
                                                     @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [GET]-'{studentId}/update/group'");
        Student resultStudent = studentService.findById(studentId);
        List<Group> groups = groupService.findAll();
        model.addAttribute("student", resultStudent);
        model.addAttribute("groups", groups);
        log.info("UPDATING... STUDENTS' {} GROUP {}", resultStudent, group);
        return "students/formForUpdateTheStudents'Group";
    }

    @PatchMapping("/{studentId}/updated/group")
    public String updateTheStudentGroup(Model model,
                                        @ModelAttribute Group group,
                                        @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [PATCH]-'{id}/updated/group'");
        Group resultGroup = groupService.findById(group.getId());
        Student student = studentService.findById(studentId);
        studentService.updateTheStudentGroup(resultGroup.getId(), student.getId());
        model.addAttribute("group", resultGroup);
        model.addAttribute("student", student);
        log.info("UPDATED THE STUDENTS' {} GROUP {} SUCCESSFULLY", student, resultGroup);
        return "students/formUpdatedTheStudentGroup";
    }

    @GetMapping("/find/by_group")
    public String createFormForFindStudentsByGroupName(Model model, @ModelAttribute("student") Student student) {
        log.info("requested-> [GET]-'find/by_group'");
        model.addAttribute("groups", groupService.findAll());
        return "students/formForFindStudentsByGroupName";
    }

    @GetMapping("/found/by_group")
    public String findStudentsByGroupName(Model model,
                                          @ModelAttribute Student student) {
        log.info("requested-> [GET]-'found/by_group'");
        List<Student> result = studentService.findStudentsByGroupId(student.getGroup().getId());
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("groupId", student.getGroup().getId());
        model.addAttribute("group", student.getGroup());
        log.info("FOUND STUDENTS {} BY GROUP ID {}", result.size(), student.getGroup().getId());
        return "students/students";
    }

    @GetMapping("/found/by_group/{groupId}")
    public String findStudentsByGroupId(Model model,
                                        @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [GET]-'/found/by_group/{groupId}'");
        List<Student> result = studentService.findStudentsByGroupId(groupId);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("group", groupId);
        log.info("FOUND STUDENT {} BY GROUP ID {}", result.size(), groupId);
        return "students/students";
    }

    @GetMapping("/find/by_name_surname")
    public String createFormForFindStudentByNameSurname(@ModelAttribute("newStudent") Student student) {
        log.info("requested-> [GET]-'find/by_name_surname'");
        return "students/formFindStudentByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findStudentByNameSurname(Model model,
                                           @ModelAttribute Student student) {
        log.info("requested-> [GET]-'found/by_name_surname'");
        List<Student> result = studentService.findStudentsByNameOrSurname(student);
        model.addAttribute("students", result);
        log.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result);
        return "students/students";
    }

    @GetMapping("/{studentId}/to_group")
    public String createFormForAssignStudentToGroup(Model model,
                                                    @ModelAttribute("student") Student student,
                                                    @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [GET]-'{studentId}/to_group'");
        Student foundStudent = studentService.findById(studentId);
        model.addAttribute("student", foundStudent);
        model.addAttribute("groups", groupService.findAll());
        log.info("FOUND STUDENT {} FOR ASSIGN TO GROUP SUCCESSFULLY", foundStudent);
        return "students/formForAssignStudentToGroup";
    }

    @PatchMapping("/{studentId}/to_group")
    public String assignStudentToGroup(Model model,
                                       @ModelAttribute Student newStudent,
                                       @PathVariable("studentId") Integer studentId) {
        log.info("requested-> [PATCH]-'{studentId}/to_group'");
        Group resultGroup = groupService.findById(newStudent.getGroup().getId());
        Student student = studentService.findById(studentId);
        groupService.assignStudentToGroup(student, resultGroup);
        model.addAttribute("assignedStudent", student);
        model.addAttribute("groupName", resultGroup);
        log.info("ASSIGNED STUDENT {} TO GROUP {} SUCCESSFULLY", student, resultGroup);
        return "students/formAssignedStudentToGroup";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAllStudents(Model model) {
        log.info("requested- [DELETE]-'/delete/all'");
        Long countStudents = studentService.count();
        studentService.deleteAll();
        model.addAttribute("count", countStudents);
        log.info("DELETED ALL STUDENT SUCCESSFULLY");
        return "students/formDeleteAllStudents";
    }
}
