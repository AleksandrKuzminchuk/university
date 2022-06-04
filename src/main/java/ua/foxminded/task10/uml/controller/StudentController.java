package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;


import java.util.List;

@Controller
@RequestMapping("/")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("students")
    @ResponseStatus(HttpStatus.OK)
    public String findAllStudents(Model model) {
        logger.info("requested-> [GET]-'/students'");
        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        logger.info("Found {} students", students.size());
        return "students/students";
    }

    @GetMapping("new_student")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveStudent(@ModelAttribute("newStudent") Student student){
        logger.info("requested-> [GET]-'/new_student'");
        return "students/formSaveStudent";
    }

    @PostMapping("saved_student")
    @ResponseStatus(HttpStatus.OK)
    public String saveStudent(Model model, @ModelAttribute Student student){
        logger.info("requested-> [POST]-'/savedStudent'");
              Student newStudent = studentService.save(student);
              model.addAttribute("student", newStudent);
              logger.info("SAVED {} SUCCESSFULLY", newStudent);
              return "students/formSavedStudent";
    }

    @GetMapping("{studentId}/update_student")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateStudent(Model model, @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [GET]-'/{}/update'", studentId);
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        logger.info("UPDATING... {}", student);
        return "students/formUpdateStudent";
    }

    @PatchMapping("{studentId}/updated_student")
    @ResponseStatus(HttpStatus.OK)
    public String updateStudent(Model model, @ModelAttribute Student student,
                                @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [PATCH]-'/{id}/updated_student'");
        studentService.updateStudent(studentId, student);
        model.addAttribute("studentUpdated", student);
        logger.info("UPDATED {} SUCCESSFULLY", student);
        return "students/formUpdatedStudent";
    }

    @DeleteMapping("{studentId}/delete_student")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStudentById(Model model, @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [DELETE]-'/{id}/delete_student'");
        Student student = studentService.findById(studentId);
        studentService.deleteById(studentId);
        model.addAttribute("deleteStudentById", student);
        logger.info("DELETED STUDENT BY ID - {}", studentId);
        return "students/formDeletedStudent";
    }

    @PatchMapping("{studentId}/delete_student_from_group")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStudentFromGroup(Model model, @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [PATCH]-'{id}/delete_student_from_group'");
        Student student = studentService.findById(studentId);
        Group group = groupService.findById(student.getGroup().getId());
        studentService.deleteTheStudentGroup(studentId);
        model.addAttribute("student", student);
        model.addAttribute("group", group);
        logger.info("DELETED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", studentId);
        return "students/formDeletedTheStudents'Group";
    }


    @DeleteMapping("delete_all_students")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllStudents(){
        logger.info("requested-> [DELETE]-'/delete'");
        studentService.deleteAll();
        logger.info("DELETED ALL STUDENTS SUCCESSFULLY");
        return "students/deleteAllStudents";
    }

    @GetMapping("find_students_by_course")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindStudentsByCourseNumber(@ModelAttribute("student") Student student){
        logger.info("requested-> [GET]-'/find_students_by_course'");
        return "students/formFindStudentsByCourse";
    }

    @GetMapping("found_students_by_course")
    @ResponseStatus(HttpStatus.OK)
    public String findStudentsByCourseNumber(Model model, @ModelAttribute Student studentCourse){
        logger.info("requested-> [GET]-'/{}/students_by_course'", studentCourse);
        List<Student> students = studentService.findByCourseNumber(studentCourse.getCourse());
        model.addAttribute("students", students);
        model.addAttribute("count", students.size());
        model.addAttribute("courseNumber", studentCourse.getCourse());
        logger.info("FOUND STUDENTS {} BY COURSE NUMBER {}", students.size(), studentCourse);
        return "students/foundStudentByCourse";
    }

    @DeleteMapping("{course}/delete_students_by_course")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStudentsByCourseNumber(Model model, @PathVariable("course") Integer studentCourse){
        logger.info("requested-> [DELETE]-'/{course}/delete_students_by_course'");
        studentService.deleteStudentsByCourseNumber(studentCourse);
        model.addAttribute("courseNumber", studentCourse);
        logger.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", studentCourse);
        return "students/formDeletedStudentsByCourseNumber";
    }

    @PatchMapping("{groupId}/delete_students_by_group")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [DELETE]-'{group}/delete_students_by_group'");
        List<Student> students = studentService.findStudentsByGroupName(groupService.findById(groupId));
        Group group = groupService.findById(groupId);
        studentService.deleteStudentsByGroupId(groupId);
        model.addAttribute("groupName", group);
        model.addAttribute("count", students.size());
        logger.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
        return "students/formDeletedStudentsByGroupName";
    }

    @GetMapping("{studentId}/update_the_student_group")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForChangeGroupTheStudent(Model model, @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [GET]-'{id}/update_the_student_group'");
        Student resultStudent = studentService.findById(studentId);
        Group group = groupService.findById(resultStudent.getGroup().getId());
        model.addAttribute("student", resultStudent);
        model.addAttribute("group", group);
        logger.info("UPDATING... STUDENTS' {} GROUP {}", resultStudent, group);
        return "students/formForUpdateTheStudents'Group";
    }

    @PatchMapping("{studentId}/updated_the_student_group")
    @ResponseStatus(HttpStatus.OK)
    public String updateTheStudentGroup(Model model, @ModelAttribute Group group, @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [Patch]-'{id}/updated_the_student_group'");
        Group resultGroup = groupService.findByGroupName(group.getName());
        Student student = studentService.findById(studentId);
        studentService.updateTheStudentGroup(resultGroup.getId(), student.getId());
        model.addAttribute("group", resultGroup);
        model.addAttribute("student", student);
        logger.info("UPDATED THE STUDENTS' {} GROUP {} SUCCESSFULLY", student, resultGroup);
        return "students/formUpdatedTheStudentGroup";
    }

    @GetMapping("delete_student_by_name_surname")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForDeleteStudent(@ModelAttribute("student") Student student){
        logger.info("requested-> [GET]-'/delete_student'");
        return "students/formForDeleteStudent";
    }

    @DeleteMapping("delete_student_by_first_and_last_name")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStudentByNameSurname(Model model, @ModelAttribute Student student){
        logger.info("requested-> [DELETE]-'/delete_student_by_first_and_last_name'");
        Student deleteStudent = studentService.findStudentByNameSurname(student);
        studentService.delete(student);
        model.addAttribute("deleteStudent", deleteStudent);
        logger.info("DELETED STUDENT {} SUCCESSFULLY", student);
        return "students/formDeletedStudentByFirstAndLastName";
    }

    @GetMapping("find_students_by_group")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindStudentsByGroupName(@ModelAttribute("groupName") Group group){
        logger.info("requested-> [GET]-'find_students_by_group'");
        return "students/formForFindStudentsByGroupName";
    }

    @GetMapping("found_students_by_group")
    @ResponseStatus(HttpStatus.OK)
    public String findStudentsByGroupName(Model model, @ModelAttribute Group group){
        logger.info("requested-> [GET]-'found_students_by_group'");
        List<Student> result = studentService.findStudentsByGroupName(group);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("groupId", result.get(0).getGroup().getId());
        model.addAttribute("group", group);
        logger.info("FOUND STUDENT {} BY GROUP NAME {}", result.size(), group.getName());
        return "students/foundStudentsByGroupName";
    }

    @GetMapping("{groupId}/found_students_by_group_id")
    @ResponseStatus(HttpStatus.OK)
    public String findStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [GET]-'{groupId}/found_students_by_group_id'");
        Group group = groupService.findById(groupId);
        List<Student> result = studentService.findStudentsByGroupName(group);
        model.addAttribute("students", result);
        model.addAttribute("count", result.size());
        model.addAttribute("group", group);
        logger.info("FOUND STUDENT {} BY GROUP NAME {}", result.size(), group.getName());
        return "students/foundStudentsByGroupName";
    }

    @GetMapping("find_student_by_name_surname")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindStudentByNameSurname(@ModelAttribute("newStudent") Student student){
        logger.info("requested-> [GET]-'find_student_by_name_surname'");
        return "students/formFindStudentByNameSurname";
    }

    @GetMapping("found_student_by_name_surname")
    @ResponseStatus(HttpStatus.OK)
    public String findStudentByNameSurname(Model model, @ModelAttribute Student student){
        logger.info("requested-> [GET]-'found_student_by_name_surname'");
        Student result = studentService.findStudentByNameSurname(student);
        model.addAttribute("students", result);
        logger.info("FOUND STUDENT {} SUCCESSFULLY", result);
        return "students/foundStudentByNameSurname";
    }

    @GetMapping("{studentId}/assign_student_to_group")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForAssignStudentToGroup(Model model,
                                                    @ModelAttribute("group") Group group,
                                                    @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [GET]-'{id}/assign_student_to_group'");
        Student student = studentService.findById(studentId);
        model.addAttribute("student", student);
        logger.info("FOUND STUDENT {} FOR ASSIGN TO GROUP SUCCESSFULLY", student);
        return "students/formForAssignStudentToGroup";
    }

    @PatchMapping("{studentId}/assigned_student_to_group")
    @ResponseStatus(HttpStatus.OK)
    public String assignStudentToGroup(Model model, @ModelAttribute Group group,
                                       @PathVariable("studentId") Integer studentId){
        logger.info("requested-> [POST]-'{id}/assigned_student_to_group'");
        Group resultGroup = groupService.findByGroupName(group.getName());
        Student student = studentService.findById(studentId);
        groupService.assignStudentToGroup(student, resultGroup);
        model.addAttribute("assignedStudent", student);
        model.addAttribute("groupName", resultGroup);
        logger.info("ASSIGNED STUDENT {} TO GROUP {} SUCCESSFULLY", student, group);
        return "students/formAssignedStudentToGroup";
    }
}
