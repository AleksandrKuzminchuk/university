package ua.foxminded.task10.uml.controller;

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
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String findAllStudents(Model model) {
        logger.info("requested-> [GET]-'/students'");
        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        logger.info("Found {} students", students.size());
        return "students/students";
    }

    @GetMapping("/new")
    public String createFormForSaveStudent(@ModelAttribute("newStudent") Student student) {
        logger.info("requested-> [GET]-'/new_student'");
        return "students/formSaveStudent";
    }

    @PostMapping("/saved")
    public String saveStudent(Model model,
                              @ModelAttribute Student student) {
        logger.info("requested-> [POST]-'/savedStudent'");
        Student newStudent = studentService.save(student);
        model.addAttribute("student", newStudent);
        logger.info("SAVED {} SUCCESSFULLY", newStudent);
        return "students/formSavedStudent";
    }

    @GetMapping("/{studentId}/update")
    public String createFormForUpdateStudent(Model model,
                                             @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [GET]-'/{studentId}/update'");
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        logger.info("UPDATING... {}", student);
        return "students/formUpdateStudent";
    }

    @PatchMapping("/{studentId}/updated")
    public String updateStudent(Model model,
                                @ModelAttribute Student student,
                                @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [PATCH]-'/{studentId}/updated'");
        studentService.updateStudent(studentId, student);
        model.addAttribute("studentUpdated", student);
        logger.info("UPDATED {} SUCCESSFULLY", student);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("/{studentId}/deleted")
    public String deleteStudentById(Model model,
                                    @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [DELETE]-'/{studentId}/delete_student'");
        Student student = studentService.findById(studentId);
        studentService.deleteById(studentId);
        model.addAttribute("deleteStudentById", student);
        logger.info("DELETED STUDENT BY ID - {}", studentId);
        return "students/formDeletedStudent";
    }

    @PatchMapping("/{studentId}/delete/from_group")
    public String deleteStudentFromGroup(Model model,
                                         @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [PATCH]-'{studentId}/delete/from_group'");
        Student student = studentService.findById(studentId);
        Group group = groupService.findById(student.getGroup().getId());
        studentService.deleteTheStudentGroup(studentId);
        model.addAttribute("student", student);
        model.addAttribute("group", group);
        logger.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", studentId);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("/delete/all/by_group/{groupId}")
    public String deleteAllStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId) {
        logger.info("requested-> [DELETE]-'/delete/all/by_group/{groupId}'");
        List<Student> students = studentService.findStudentsByGroupId(groupId);
        studentService.deleteStudentsByGroupId(groupId);
        model.addAttribute("count", students.size());
        logger.info("DELETED {} BY GROUP ID - {} STUDENTS SUCCESSFULLY", students.size(), groupId);
        return "students/deletedAllStudentsByGroupId";
    }

    @GetMapping("/find/by_course")
    public String createFormForFindStudentsByCourseNumber(@ModelAttribute("student") Student student) {
        logger.info("requested-> [GET]-'/find/by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("/found/by_course")
    public String findStudentsByCourseNumber(Model model,
                                             @ModelAttribute Student studentCourse) {
        logger.info("requested-> [GET]-'/found/by_course'");
        List<Student> students = studentService.findByCourseNumber(studentCourse.getCourse());
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        model.addAttribute("courseNumber", studentCourse.getCourse());
        logger.info("FOUND STUDENTS {} BY COURSE NUMBER {}", students.size(), studentCourse);
        return "students/students";
    }

    @DeleteMapping("/delete/by_course/{course}")
    public String deleteStudentsByCourseNumber(Model model,
                                               @PathVariable("course") Integer studentCourse) {
        logger.info("requested-> [DELETE]-'/delete/by_course/{course}'");
        studentService.deleteStudentsByCourseNumber(studentCourse);
        model.addAttribute("courseNumber", studentCourse);
        logger.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", studentCourse);
        return "students/formDeletedStudentsByCourseNumber";
    }

    @PatchMapping("/delete/by_group/{groupId}")
    public String deleteStudentsByGroupId(Model model,
                                          @PathVariable("groupId") Integer groupId) {
        logger.info("requested-> [PATCH]-'/delete/by_group/{groupId}'");
        List<Student> students = studentService.findStudentsByGroupName(groupService.findById(groupId));
        Group group = groupService.findById(groupId);
        studentService.deleteStudentsByGroupId(groupId);
        model.addAttribute("groupName", group);
        model.addAttribute("count", students.size());
        logger.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
        return "students/formDeletedStudentsByGroupName";
    }

    @GetMapping("/{studentId}/update/group")
    public String createFormForChangeGroupTheStudent(Model model,
                                                     @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [GET]-'{studentId}/update/group'");
        Student resultStudent = studentService.findById(studentId);
        Group group = groupService.findById(resultStudent.getGroup().getId());
        model.addAttribute("student", resultStudent);
        model.addAttribute("group", group);
        logger.info("UPDATING... STUDENTS' {} GROUP {}", resultStudent, group);
        return "students/formForUpdateTheStudents'Group";
    }

    @PatchMapping("/{studentId}/updated/group")
    public String updateTheStudentGroup(Model model,
                                        @ModelAttribute Group group,
                                        @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [PATCH]-'{id}/updated/group'");
        Group resultGroup = groupService.findByGroupName(group.getName());
        Student student = studentService.findById(studentId);
        studentService.updateTheStudentGroup(resultGroup.getId(), student.getId());
        model.addAttribute("group", resultGroup);
        model.addAttribute("student", student);
        logger.info("UPDATED THE STUDENTS' {} GROUP {} SUCCESSFULLY", student, resultGroup);
        return "students/formUpdatedTheStudentGroup";
    }

    @GetMapping("/find/by_group")
    public String createFormForFindStudentsByGroupName(@ModelAttribute("groupName") Group group) {
        logger.info("requested-> [GET]-'find/by_group'");
        return "students/formForFindStudentsByGroupName";
    }

    @GetMapping("/found/by_group")
    public String findStudentsByGroupName(Model model,
                                          @ModelAttribute Group group) {
        logger.info("requested-> [GET]-'found/by_group'");
        List<Student> result = studentService.findStudentsByGroupName(group);
        group.setId(result.get(0).getGroup().getId());
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("groupId", result.get(0).getGroup().getId());
        model.addAttribute("group", group);
        logger.info("FOUND STUDENT {} BY GROUP NAME {}", result.size(), group.getName());
        return "students/students";
    }

    @GetMapping("/found/by_group/{groupId}")
    public String findStudentsByGroupId(Model model,
                                        @PathVariable("groupId") Integer groupId) {
        logger.info("requested-> [GET]-'/found/by_group/{groupId}'");
        List<Student> result = studentService.findStudentsByGroupId(groupId);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("group", groupId);
        logger.info("FOUND STUDENT {} BY GROUP ID {}", result.size(), groupId);
        return "students/students";
    }

    @GetMapping("/find/by_name_surname")
    public String createFormForFindStudentByNameSurname(@ModelAttribute("newStudent") Student student) {
        logger.info("requested-> [GET]-'find/by_name_surname'");
        return "students/formFindStudentByNameSurname";
    }

    @GetMapping("/found/by_name_surname")
    public String findStudentByNameSurname(Model model,
                                           @ModelAttribute Student student) {
        logger.info("requested-> [GET]-'found/by_name_surname'");
        List<Student> result = studentService.findStudentsByNameOrSurname(student);
        model.addAttribute("students", result);
        logger.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result);
        return "students/students";
    }

    @GetMapping("/{studentId}/to_group")
    public String createFormForAssignStudentToGroup(Model model,
                                                    @ModelAttribute("group") Group group,
                                                    @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [GET]-'{studentId}/to_group'");
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        logger.info("FOUND STUDENT {} FOR ASSIGN TO GROUP SUCCESSFULLY", student);
        return "students/formForAssignStudentToGroup";
    }

    @PatchMapping("/{studentId}/to_group")
    public String assignStudentToGroup(Model model,
                                       @ModelAttribute Group group,
                                       @PathVariable("studentId") Integer studentId) {
        logger.info("requested-> [PATCH]-'{studentId}/to_group'");
        Group resultGroup = groupService.findByGroupName(group.getName());
        Student student = studentService.findById(studentId);
        groupService.assignStudentToGroup(student, resultGroup);
        model.addAttribute("assignedStudent", student);
        model.addAttribute("groupName", resultGroup);
        logger.info("ASSIGNED STUDENT {} TO GROUP {} SUCCESSFULLY", student, group);
        return "students/formAssignedStudentToGroup";
    }

    @DeleteMapping("/deleted/all")
    public String deleteAllStudents(Model model){
        logger.info("requested- [DELETE]-'/delete/all'");
        List<Student> students = studentService.findAll();
        studentService.deleteAll();
        model.addAttribute("count", students.size());
        logger.info("DELETED ALL STUDENT SUCCESSFULLY");
        return "students/formDeleteAllStudents";
    }
}
