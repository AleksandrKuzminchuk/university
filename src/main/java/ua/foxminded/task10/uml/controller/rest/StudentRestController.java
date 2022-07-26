package ua.foxminded.task10.uml.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.service.StudentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public StudentsResponse findAll() {
        log.info("requested-> [GET]-'/api/students'");
        List<StudentDTO> studentsDTO = studentService.findAll();
        return new StudentsResponse(studentsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDTO save(@RequestBody @Valid StudentDTO studentDTO) {
        log.info("requested-> [POST]-'/api/students/save'");
        StudentDTO savedStudentDTO = studentService.save(studentDTO);
        log.info("SAVED {} SUCCESSFULLY", savedStudentDTO);
        return savedStudentDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDTO update(@RequestBody @Valid StudentDTO studentDTO,
                                             @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/students/update/{id}'");
        studentDTO.setId(id);
        studentService.update(studentDTO);
        log.info("UPDATED {} SUCCESSFULLY", studentDTO);
        return studentDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/students/{id}/delete'");
        studentService.deleteById(id);
        log.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/delete/from_group")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteFromGroup(@PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/students/{id}/delete/from_group'");
        studentService.deleteGroup(id);
        log.info("DELETED GROUP FROM STUDENT SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all/by_group/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAllByGroupId(@RequestBody StudentDTO studentDTO) {
        log.info("requested-> [DELETE]-'/api/students/delete/all/by_group/{groupId}'");
        studentService.deleteByGroupId(studentDTO.getGroup().getId());
        log.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", studentDTO.getGroup().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_course/{course}")
    @ResponseStatus(HttpStatus.FOUND)
    public StudentsResponse findByCourseNumber(@PathVariable("course") Integer course) {
        log.info("requested-> [GET]-'/api/students/found/by_course/{course}'");
        List<StudentDTO> studentsDTO = studentService.findByCourseNumber(course);
        return new StudentsResponse(studentsDTO);
    }

    @DeleteMapping("/delete/by_course/{course}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAllByCourse(@PathVariable("course") Integer course) {
        log.info("requested-> [DELETE]-'/api/students/delete/by_course/{course}'");
        studentService.deleteByCourseNumber(course);
        log.info("DELETED STUDENTS BY COURSE - {} SUCCESSFULLY", course);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_group/{groupId}")
    @ResponseStatus(HttpStatus.FOUND)
    public StudentsResponse findByGroupId(@PathVariable("groupId") Integer groupId) {
        log.info("requested-> [GET]-'/api/students/found/by_group/{groupId}'");
        List<StudentDTO> studentsDTO = studentService.findByGroupId(groupId);
        return new StudentsResponse(studentsDTO);
    }

    @GetMapping("/find/by_name_surname")
    @ResponseStatus(HttpStatus.FOUND)
    public StudentsResponse findByNameOrSurname(@RequestBody StudentDTO studentDTO) {
        log.info("requested-> [GET]-'/api/students/find/by_name_surname'");
        List<StudentDTO> students = studentService.findByNameOrSurname(studentDTO.getFirstName(), studentDTO.getLastName());
        return new StudentsResponse(students);
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAll() {
        log.info("requested- [DELETE]-'/api/students/delete/all'");
        studentService.deleteAll();
        log.info("DELETED ALL SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

}
