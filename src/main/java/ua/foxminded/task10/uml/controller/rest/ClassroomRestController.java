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
import ua.foxminded.task10.uml.dto.ClassroomCreateDTO;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.dto.mapper.ClassroomMapper;
import ua.foxminded.task10.uml.dto.response.ClassroomResponse;
import ua.foxminded.task10.uml.service.ClassroomService;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.validations.ClassroomValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classrooms")
@Api(value = "classroom-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Classroom API"})
public class ClassroomRestController {

    private final ClassroomService classroomService;
    private final ClassroomValidator classroomValidator;
    private final ClassroomMapper classroomMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all classrooms",
            notes = "Finding all classrooms from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ClassroomResponse.class,
            httpMethod = "GET",
            responseContainer = "ClassroomResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found all classrooms successfully",
                    responseContainer = "ClassroomResponse",
                    response = ClassroomResponse.class)})
    public ClassroomResponse findAll() {
        log.info("requested-> [GET]-'/api/classrooms'");
        List<ClassroomDTO> classroomsDTO = classroomService.findAll();
        return new ClassroomResponse(classroomsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save a classroom",
            notes = "Saving the unique classroom to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ClassroomDTO.class,
            httpMethod = "POST",
            responseContainer = "ClassroomDTO")
    @ApiResponses(value = {@ApiResponse(
            code = 201,
            message = "The classroom created successfully",
            response = ClassroomDTO.class,
            responseContainer = "ClassroomDTO")})
    public ClassroomDTO save(@ApiParam(value = "ClassroomDTO instance") @RequestBody @Valid ClassroomCreateDTO classroomCreateDTO,
                             BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api/classrooms/save'");
        ClassroomDTO saveClassroomDTO = classroomMapper.convertToClassroomDTO(classroomCreateDTO);
        classroomValidator.validate(saveClassroomDTO, bindingResult);
        extractedErrors(bindingResult);
        ClassroomDTO savedClassroomDTO = classroomService.save(saveClassroomDTO);
        log.info("SAVED {} SUCCESSFULLY", savedClassroomDTO);
        return savedClassroomDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update a classroom",
            notes = "Updating a classroom from DB to new Classroom number must be unique and not repeat",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ClassroomDTO.class,
            httpMethod = "PATCH",
            responseContainer = "ClassroomDTO")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "The classroom updated successfully",
            response = ClassroomDTO.class,
            responseContainer = "ClassroomDTO")})
    public ClassroomDTO update(@ApiParam(value = "Classroom - Id") @PathVariable("id") Integer id,
                               @ApiParam(value = "ClassroomDTO instance")
                               @RequestBody @Valid ClassroomCreateDTO classroomCreateDTO, BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api/classrooms/update/{id}'");
        ClassroomDTO updateClassroomDTO = classroomMapper.convertToClassroomDTO(classroomCreateDTO);
        classroomValidator.validate(updateClassroomDTO, bindingResult);
        extractedErrors(bindingResult);
        updateClassroomDTO.setId(id);
        classroomService.update(updateClassroomDTO);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", updateClassroomDTO);
        return updateClassroomDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete classroom",
            notes = "Deleting a classroom from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Classroom deleted successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteById(@ApiParam(value = "Classroom - Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/classrooms/{id}/delete'");
        classroomService.deleteById(id);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_number/{number}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find classroom",
            notes = "Finding a classroom by number from DB",
            nickname = "findByNumber",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ClassroomDTO.class,
            responseContainer = "ClassroomDTO")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Found classroom by number successfully",
            response = ClassroomDTO.class,
            responseContainer = "ClassroomDTO")})
    public ClassroomDTO findByNumber(@ApiParam(value = "Classroom number") @PathVariable("number") Integer classroomNumber) {
        log.info("requested-> [GET]-'/api/classrooms/find/by_number'");
        ClassroomDTO result = classroomService.findByNumber(classroomNumber);
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result, classroomNumber);
        return result;
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all classrooms",
            notes = "Deleting all classroom from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all classrooms successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deletedAll() {
        log.info("requested- [DELETE]-'/api/classrooms/deleted/all'");
        classroomService.deleteAll();
        log.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }


}
