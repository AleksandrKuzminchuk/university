package ua.foxminded.task10.uml.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.mapper.GroupMapper;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.dto.mapper.StudentMapper;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;
    private final GroupService groupService;



    @GetMapping
    public StudentsResponse findAll(){
        log.info("requested-> [GET]-'/api/students'");
        return new StudentsResponse(studentService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<StudentDTO> save(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult){
        log.info("requested-> [POST]-'/api/students/save'");
        StudentDTO savedStudentDTO = studentService.save(studentDTO);
        log.info("SAVED {} SUCCESSFULLY", savedStudentDTO);
        return new ResponseEntity<>(savedStudentDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<StudentDTO> update(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult,
                                             @PathVariable("id") Integer id){
        log.info("requested-> [PATCH]-'/api/students/update'");
        studentDTO.setId(id);
        StudentDTO updatedStudentDTO = studentService.update(studentDTO);
        log.info("UPDATED {} SUCCESSFULLY", updatedStudentDTO);
        return ResponseEntity.ok(updatedStudentDTO);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<StudentDTO> deleteById(@PathVariable("id") Integer id){
        log.info("requested-> [DELETE]-'/api/students/{id}/delete'");
        StudentDTO studentDTO = studentService.findById(id);
        studentService.deleteById(id);
        log.info("DELETED {} SUCCESSFULLY", studentDTO);
        return ResponseEntity.ok(studentDTO);
    }

    @PatchMapping("/{id}/delete/from_group")
    public ResponseEntity<StudentDTO> deleteFromGroup(@PathVariable("id") Integer id){
        log.info("requested-> [PATCH]-'/api/students/{id}/delete/from_group'");
        StudentDTO studentDTO = studentService.deleteGroup(id);
        log.info("DELETED {} FROM {} {} SUCCESSFULLY", studentDTO.getGroup(), studentDTO.getFirstName(), studentDTO.getLastName());
        return ResponseEntity.ok(studentDTO);
    }

    @DeleteMapping("/delete/all/by_group/")
    public ResponseEntity<Long> deleteAllByGroupId(@RequestBody StudentDTO studentDTO){
        log.info("requested-> [DELETE]-'/api/students/delete/all/by_group/{groupId}'");
        GroupDTO groupDTO = groupService.findByName(studentDTO.getGroup().getName());
        Long countStudentsByGroupId = studentService.countByGroupId(groupDTO.getId());
        studentService.deleteByGroupId(groupDTO.getId());
        log.info("DELETED {} STUDENTS BY GROUP ID - {} SUCCESSFULLY", countStudentsByGroupId, groupDTO.getId());
        return new ResponseEntity<>(countStudentsByGroupId, HttpStatus.OK);
    }

    @GetMapping("/find/by_course/{course}")
    public StudentsResponse findByCourseNumber(@PathVariable("course") Integer course){
        log.info("requested-> [GET]-'/api/students/found/by_course/{course}'");
        return new StudentsResponse(studentService.findByCourseNumber(course));
    }

    @DeleteMapping("/delete/by_course/{course}")
    public ResponseEntity<Long> deleteAllByCourse(@PathVariable("course") Integer course){
        log.info("requested-> [DELETE]-'/api/students/delete/by_course/{course}'");
        Long countStudentsByCourse = studentService.countByCourse(course);
        studentService.deleteByCourseNumber(course);
        log.info("DELETED {} BY COURSE - {} SUCCESSFULLY", countStudentsByCourse, course);
        return new ResponseEntity<>(countStudentsByCourse, HttpStatus.OK);
    }

    @GetMapping("/find/by_group/{groupId}")
    public StudentsResponse findByGroupId(@PathVariable("groupId") Integer groupId){
        log.info("requested-> [GET]-'/api/students/found/by_group/{groupId}'");
        return new StudentsResponse(studentService.findByGroupId(groupId));
    }

    @GetMapping("/find/by_name_surname")
    public StudentsResponse findByNameOrSurname(@RequestBody StudentDTO studentDTO){
        log.info("requested-> [GET]-'/api/students/find/by_name_surname'");
        return new StudentsResponse(studentService.findByNameOrSurname(studentDTO.getFirstName(), studentDTO.getLastName()));
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> deleteAll(){
        log.info("requested- [DELETE]-'/api/students/delete/all'");
        Long countStudents = studentService.count();
        studentService.deleteAll();
        log.info("DELETED ALL SUCCESSFULLY");
        return ResponseEntity.ok(countStudents);
    }

}
