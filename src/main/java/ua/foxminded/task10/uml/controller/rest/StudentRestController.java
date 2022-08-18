package ua.foxminded.task10.uml.controller.rest;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.dto.mapper.StudentMapper;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.service.StudentService;
import ua.foxminded.task10.uml.util.errors.ErrorResponse;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@Api(value = "student-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Student API"})
public class StudentRestController {

    private final StudentService service;
    private final StudentMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all students",
            notes = "Finding all students from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = StudentsResponse.class,
            httpMethod = "GET",
            responseContainer = "StudentsResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found all students successfully",
                    responseContainer = "StudentsResponse",
                    response = StudentsResponse.class)})
    public StudentsResponse findAll() {
        log.info("requested-> [GET]-'/api/students'");
        List<StudentDTO> studentsDTO = service.findAll();
        return new StudentsResponse(studentsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save student",
            notes = "Saving student to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = StudentDTO.class,
            httpMethod = "POST",
            responseContainer = "StudentDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "The student created successfully",
                    response = StudentDTO.class,
                    responseContainer = "StudentDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Student not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse")})
    public StudentDTO save(@ApiParam(value = "StudentCreateDTO instance") @RequestBody @Valid StudentCreateDTO studentCreateDTO,
                           BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api/students/save'");
        extractedErrors(bindingResult);
        StudentDTO saveStudentDTO = mapper.map(studentCreateDTO);
        StudentDTO savedStudentDTO = service.save(saveStudentDTO);
        log.info("SAVED {} SUCCESSFULLY", savedStudentDTO);
        return savedStudentDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update student",
            notes = "Updating student from DB to new Student",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = StudentDTO.class,
            httpMethod = "PATCH",
            responseContainer = "StudentDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The student updated successfully",
                    response = StudentDTO.class,
                    responseContainer = "StudentDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Student not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Student not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 401,
                    message = "Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public StudentDTO update(@ApiParam("Student - Id") @PathVariable("id") Integer id,
                             @ApiParam("StudentUpdateDTO instance") @RequestBody @Valid StudentUpdateDTO studentUpdateDTO,
                             BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api/students/update/{id}'");
        extractedErrors(bindingResult);
        StudentDTO updateStudentDTO = mapper.map(studentUpdateDTO);
        updateStudentDTO.setId(id);
        service.update(updateStudentDTO);
        log.info("UPDATED {} SUCCESSFULLY", updateStudentDTO);
        return updateStudentDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete student",
            notes = "Deleting student from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Student deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Student not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteById(@ApiParam("Student - Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/students/{id}/delete'");
        service.deleteById(id);
        log.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/delete/from_group")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete group",
            notes = "Deleting group from student by Id",
            nickname = "deleteFromGroup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "PATCH",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Group deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Student not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteFromGroup(@PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/students/{id}/delete/from_group'");
        service.deleteGroup(id);
        log.info("DELETED GROUP FROM STUDENT SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all/by_group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete students by group",
            notes = "Deleting students from DB by group Id",
            nickname = "deleteFromGroup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Deleted students by group Id successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteAllByGroupId(@ApiParam("Group Id") @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [DELETE]-'/api/students/delete/all/by_group/{groupId}'");
        service.deleteByGroupId(groupId);
        log.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_course/{course}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find students by course",
            notes = "Finding students by course",
            nickname = "findByCourseNumber",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse")
    @ApiResponses(value = @ApiResponse(
            code = 200,
            message = "Found students by course successfully",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse"))
    public StudentsResponse findByCourseNumber(@ApiParam("Student course") @PathVariable("course") Integer course) {
        log.info("requested-> [GET]-'/api/students/found/by_course/{course}'");
        List<StudentDTO> studentsDTO = service.findByCourseNumber(course);
        return new StudentsResponse(studentsDTO);
    }

    @DeleteMapping("/delete/by_course/{course}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete students by course",
            notes = "Deleting students from DB by course",
            nickname = "deleteAllByCourse",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = @ApiResponse(
            code = 200,
            message = "Deleted students by course successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>"))
    public ResponseEntity<?> deleteAllByCourse(@PathVariable("course") Integer course) {
        log.info("requested-> [DELETE]-'/api/students/delete/by_course/{course}'");
        service.deleteByCourseNumber(course);
        log.info("DELETED STUDENTS BY COURSE - {} SUCCESSFULLY", course);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find students by group",
            notes = "Finding students by group Id",
            nickname = "findByGroupId",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found students by group Id successfully",
                    response = StudentsResponse.class,
                    responseContainer = "StudentsResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public StudentsResponse findByGroupId(@ApiParam("Group Id") @PathVariable("groupId") Integer groupId) {
        log.info("requested-> [GET]-'/api/students/found/by_group/{groupId}'");
        List<StudentDTO> studentsDTO = service.findByGroupId(groupId);
        return new StudentsResponse(studentsDTO);
    }

    @GetMapping("/find/name-or-surname")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find students by name or surname",
            notes = "Finding students name or surname from DB",
            nickname = "findByNameOrSurname",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse")
    @ApiResponses(value = @ApiResponse(
            code = 200,
            message = "Found students by name or surname successfully",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse"))
    public StudentsResponse findByNameOrSurname(@ApiParam(value = "Student") @RequestHeader Map<String, String> student) {
        log.info("requested-> [GET]-'/api/students/find/name-or-surname'");
        List<StudentDTO> students = service.findByNameOrSurname(student.get("firstName"), student.get("lastName"));
        log.info("Found students {} by name or surname SUCCESSFULLY", students.size());
        return new StudentsResponse(students);
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all students",
            notes = "Deleting all students from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all students successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteAll() {
        log.info("requested- [DELETE]-'/api/students/delete/all'");
        service.deleteAll();
        log.info("DELETED ALL SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }
}
