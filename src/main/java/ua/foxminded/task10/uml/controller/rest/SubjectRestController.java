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
import ua.foxminded.task10.uml.dto.mapper.SubjectMapper;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherResponse;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.util.errors.ErrorResponse;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;
import ua.foxminded.task10.uml.util.validations.SubjectValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
@Api(value = "subject-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Subject API"})
public class SubjectRestController {

    private final SubjectService service;
    private final SubjectValidator validator;
    private final SubjectMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all subjects",
            notes = "Finding all subjects from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = SubjectResponse.class,
            httpMethod = "GET",
            responseContainer = "SubjectResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found all subjects successfully",
                    responseContainer = "SubjectResponse",
                    response = SubjectResponse.class)})
    public SubjectResponse findAll() {
        log.info("requested-> [GET]-'/api/subjects'");
        List<SubjectDTO> subjectsDTO = service.findAll();
        return new SubjectResponse(subjectsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save unique subject",
            notes = "Saving unique subject to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = SubjectDTO.class,
            httpMethod = "POST",
            responseContainer = "SubjectDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "The subject created successfully",
                    response = SubjectDTO.class,
                    responseContainer = "SubjectDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Subject not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Subject is already taken",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public SubjectDTO save(@ApiParam(value = "SubjectDTO instance") @RequestBody @Valid SubjectCreateDTO subjectCreateDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api//subjects/save'");
        SubjectDTO saveSubjectDTO = mapper.map(subjectCreateDTO);
        validator.validate(saveSubjectDTO, bindingResult);
        extractedErrors(bindingResult);
        SubjectDTO savedSubjectDTO = service.save(saveSubjectDTO);
        log.info("SAVED {} SUCCESSFULLY", savedSubjectDTO);
        return savedSubjectDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update subject",
            notes = "Updating subject from DB to new Subject name must be unique and not repeat",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = SubjectDTO.class,
            httpMethod = "PATCH",
            responseContainer = "SubjectDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The subject updated successfully",
                    response = SubjectDTO.class,
                    responseContainer = "SubjectDTO"),
            @ApiResponse(
                    code = 404,
                    message = "Subject is already taken",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 400,
                    message = "Subject not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse")})
    public SubjectDTO update(@ApiParam(value = "Subject Id") @PathVariable("id") Integer id,
                             @ApiParam(value = "SubjectDTO instance") @RequestBody @Valid SubjectCreateDTO subjectCreateDTO,
                             BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api//subjects/update/{id}'");
        SubjectDTO updateSubjectDTO = mapper.map(subjectCreateDTO);
        validator.validate(updateSubjectDTO, bindingResult);
        extractedErrors(bindingResult);
        updateSubjectDTO.setId(id);
        service.update(updateSubjectDTO);
        log.info("UPDATED {} SUCCESSFULLY", updateSubjectDTO);
        return updateSubjectDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete subject",
            notes = "Deleting subject from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Subject deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Subject not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteById(@ApiParam(value = "Subject Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api//subjects/{id}/deleted'");
        service.deleteById(id);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all subjects",
            notes = "Deleting all subjects from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all subjects successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/subjects/delete/all'");
        service.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_name")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find subject",
            notes = "Finding subject by name from DB",
            nickname = "findByName",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = SubjectDTO.class,
            responseContainer = "SubjectDTO")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Found subject by name successfully",
            response = SubjectDTO.class,
            responseContainer = "SubjectDTO")})
    public ResponseEntity<SubjectDTO> findByName(@ApiParam("Subject name") @RequestHeader String subjectName) {
        log.info("requested-> [GET]-'/api/subjects/find/by_name'");
        SubjectDTO result = service.findByName(subjectName);
        log.info("FOUND {} SUBJECT BY NAME {}", result, subjectName);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{subjectId}/add/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Add teacher to subject",
            notes = "Adding teacher by Id to subject by Id",
            nickname = "addTeacher",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = TeacherDTO.class,
            httpMethod = "POST",
            responseContainer = "TeacherDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Teacher added to subject successfully",
                    response = TeacherDTO.class,
                    responseContainer = "TeacherDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Subject not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 401,
                    message = "Teacher not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "The subject already has the teacher",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public TeacherDTO addTeacher(@ApiParam(value = "Subject Id") @PathVariable("subjectId") Integer subjectId,
                                 @ApiParam(value = "Teacher Id") @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [POST]-'/api/subjects/{id}/add/teacher'");
        TeacherDTO teacherDTOToBeAdded = service.addTeacher(subjectId, teacherId);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return teacherDTOToBeAdded;
    }

    @GetMapping("/{id}/find/teachers")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find teachers",
            notes = "Finding teachers by subject Id",
            nickname = "findTeachers",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = TeacherResponse.class,
            responseContainer = "TeacherResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found teachers by subject Id successfully",
                    response = TeacherResponse.class,
                    responseContainer = "TeacherResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Subject by id not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public TeacherResponse findTeachers(@ApiParam(value = "Subject Id") @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/subjects/{id}/find/teachers'");
        List<TeacherDTO> teachersDTO = service.findTeachers(id);
        return new TeacherResponse(teachersDTO);
    }

    @PatchMapping("/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update teacher to subject",
            notes = "Updating teacher by Id to subject by Id",
            nickname = "updateTeacher",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = TeacherDTO.class,
            httpMethod = "PATCH",
            responseContainer = "TeacherDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Teacher updated to subject successfully",
                    response = TeacherDTO.class,
                    responseContainer = "TeacherDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Subject not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 401,
                    message = "Teacher not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "The subject already has the teacher",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public TeacherDTO updateTeacher(@ApiParam(value = "Teacher new Id") @PathVariable("newTeacherId") Integer newTeacherId,
                                    @ApiParam(value = "Teacher old Id") @PathVariable("oldTeacherId") Integer oldTeacherId,
                                    @ApiParam(value = "Subject Id") @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/api/subjects/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}'");
        TeacherDTO teacherDTOToBeUpdated = service.updateTeacher(subjectId, oldTeacherId, newTeacherId);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacherId);
        return teacherDTOToBeUpdated;
    }

    @DeleteMapping("/{subjectId}/delete/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete teacher from subject",
            notes = "Deleting teacher by Id from subject by Id",
            nickname = "deleteTeacher",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Teacher deleted from subject successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 400,
                    message = "Subject not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 401,
                    message = "Teacher not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Can't delete because the subject hasn't the teacher",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteTeacher(@ApiParam(value = "Subject Id") @PathVariable("subjectId") Integer subjectId,
                                           @ApiParam(value = "Teacher Id") @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/api/subjects/{subjectId}/delete/{teacherId}/teacher'");
        service.deleteTeacher(subjectId, teacherId);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return ResponseEntity.ok().build();
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }

}
