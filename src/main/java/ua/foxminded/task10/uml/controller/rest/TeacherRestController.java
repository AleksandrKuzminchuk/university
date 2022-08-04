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
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherCreateDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.mapper.TeacherMapper;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherResponse;
import ua.foxminded.task10.uml.service.TeacherService;
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
@RequestMapping("/api/teachers")
@Api(value = "teacher-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Teacher API"})
public class TeacherRestController {

    private final TeacherService service;
    private final TeacherMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all teachers",
            notes = "Finding all teachers from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = TeacherResponse.class,
            httpMethod = "GET",
            responseContainer = "TeacherResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found all teachers successfully",
                    responseContainer = "TeacherResponse",
                    response = TeacherResponse.class)})
    public TeacherResponse findAll() {
        log.info("requested-> [GET]-'/api/teachers'");
        List<TeacherDTO> teachersDTO = service.findAll();
        return new TeacherResponse(teachersDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save teacher",
            notes = "Saving teacher to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = TeacherDTO.class,
            httpMethod = "POST",
            responseContainer = "TeacherDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "The teacher created successfully",
                    response = TeacherDTO.class,
                    responseContainer = "TeacherDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Teacher not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse")})
    public TeacherDTO save(@ApiParam(value = "TeacherCreateDTO instance") @RequestBody @Valid TeacherCreateDTO teacherCreateDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api/teachers/save'");
        extractedErrors(bindingResult);
        TeacherDTO saveTeacherDTO = mapper.map(teacherCreateDTO);
        TeacherDTO savedTeacherDTO = service.save(saveTeacherDTO);
        log.info("SAVED {} SUCCESSFULLY", savedTeacherDTO);
        return savedTeacherDTO;
    }

    @DeleteMapping("{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete teacher",
            notes = "Deleting teacher from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Teacher deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Teacher not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteById(@ApiParam(value = "Teacher Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/teachers/{id}/delete'");
        service.deleteById(id);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update teacher",
            notes = "Updating teacher from DB to new Teacher",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = TeacherDTO.class,
            httpMethod = "PATCH",
            responseContainer = "TeacherDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The teacher updated successfully",
                    response = TeacherDTO.class,
                    responseContainer = "TeacherDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Teacher not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse"),
            @ApiResponse(
                    code = 400,
                    message = "Teacher not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public TeacherDTO update(@ApiParam(value = "Teacher Id") @PathVariable("id") Integer id,
                             @ApiParam("TeacherCreateDTO instance") @RequestBody @Valid TeacherCreateDTO teacherCreateDTO,
                             BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api/teachers/update/{id}'");
        extractedErrors(bindingResult);
        TeacherDTO updateTeacherDTO = mapper.map(teacherCreateDTO);
        updateTeacherDTO.setId(id);
        service.update(updateTeacherDTO);
        log.info("UPDATED {} SUCCESSFULLY", updateTeacherDTO);
        return updateTeacherDTO;
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all teachers",
            notes = "Deleting all teachers from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all teachers successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/teachers/delete/all'");
        service.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/name-or-surname")   //TODO
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find students by name or surname",
            notes = "Finding students name or surname from DB",
            nickname = "findByNameOrSurname",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = TeacherResponse.class,
            responseContainer = "TeacherResponse")
    @ApiResponses(value = @ApiResponse(
            code = 200,
            message = "Found teachers by name or surname successfully",
            response = TeacherResponse.class,
            responseContainer = "TeacherResponse"))
    public TeacherResponse findByNameOrSurname(@ApiParam(value = "Teacher") @RequestHeader Map<String, String> teacher) {
        log.info("requested-> [GET]-'/api/teachers/find/name-or-surname'");
        List<TeacherDTO> teachersDTO = service.findByNameOrSurname(teacher.get("name"), teacher.get("surname"));
        return new TeacherResponse(teachersDTO);
    }

    @GetMapping("/{id}/find/subjects")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find subjects",
            notes = "Finding subjects by teacher Id",
            nickname = "findSubjects",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET",
            response = StudentsResponse.class,
            responseContainer = "StudentsResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found subjects by teacher Id successfully",
                    response = StudentsResponse.class,
                    responseContainer = "StudentsResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Teacher by id not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public SubjectResponse findSubjects(@ApiParam(value = "Teacher Id") @PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/teachers/{id}/find/subjects'");
        List<SubjectDTO> subjectsDTO = service.findSubjects(id);
        return new SubjectResponse(subjectsDTO);
    }

    @PostMapping("/{teacherId}/add/subject/{subjectId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Add subject to teacher",
            notes = "Adding subject by Id to teacher by Id",
            nickname = "addSubject",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = SubjectDTO.class,
            httpMethod = "POST",
            responseContainer = "SubjectDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Subject added to teacher successfully",
                    response = SubjectDTO.class,
                    responseContainer = "Teacher"),
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
                    message = "The teacher already has the subject",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public SubjectDTO addSubject(@ApiParam(value = "Teacher Id") @PathVariable("teacherId") Integer teacherId,
                                 @ApiParam(value = "Subject Id") @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [POST]-'/api/teachers/{teacherId}/add/subject/{subjectId}'");
        SubjectDTO subjectDTOToBeAdded = service.addSubject(teacherId, subjectId);
        log.info("ADDED TEACHER BY ID - {} TO SUBJECT {} SUCCESSFULLY", teacherId, subjectId);
        return subjectDTOToBeAdded;
    }

    @PatchMapping("/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update subject to teacher",
            notes = "Updating subject by Id to teacher by Id",
            nickname = "updateSubject",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = SubjectDTO.class,
            httpMethod = "PATCH",
            responseContainer = "SubjectDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Subject updated to teacher successfully",
                    response = SubjectDTO.class,
                    responseContainer = "SubjectDTO"),
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
                    message = "The teacher already has the subject",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public SubjectDTO updateSubject(@ApiParam(value = "Subject new Id") @PathVariable("newSubjectId") Integer newSubjectId,
                                    @ApiParam(value = "Subject old Id") @PathVariable("oldSubjectId") Integer oldSubjectId,
                                    @ApiParam(value = "Teacher Id") @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'/api/teachers/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}'");
        SubjectDTO subjectDTOToBeUpdated = service.updateSubject(teacherId, oldSubjectId, newSubjectId);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY NAME - {}", teacherId, oldSubjectId, newSubjectId);
        return subjectDTOToBeUpdated;
    }

    @DeleteMapping("/{teacherId}/delete/{subjectId}/subject")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete subject from teacher",
            notes = "Deleting subject by Id from teacher by Id",
            nickname = "deleteSubject",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Subject deleted from teacher successfully",
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
                    message = "Can't delete because the teacher hasn't the subject",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteSubject(@ApiParam(value = "Teacher Id") @PathVariable("teacherId") Integer teacherId,
                                           @ApiParam(value = "Subject Id") @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/api/teachers/{teacherId}/delete/{subjectId}/subject'");
        service.deleteSubject(teacherId, subjectId);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return ResponseEntity.ok().build();
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }
}
