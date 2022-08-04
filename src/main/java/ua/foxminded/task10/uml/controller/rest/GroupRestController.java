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
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.dto.GroupCreateDTO;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.mapper.GroupMapper;
import ua.foxminded.task10.uml.dto.response.GroupResponse;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.util.errors.ErrorResponse;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;
import ua.foxminded.task10.uml.util.validations.GroupValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
@Api(value = "group-rest-controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Group API"})
public class GroupRestController {
    private final GroupService service;
    private final GroupValidator validator;
    private final GroupMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get all groups",
            notes = "Finding all groups from DB",
            nickname = "findAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GroupResponse.class,
            httpMethod = "GET",
            responseContainer = "GroupResponse")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found all groups successfully",
                    responseContainer = "GroupResponse",
                    response = GroupResponse.class)})
    public GroupResponse findAll() {
        log.info("requested- [GET]-'/api/groups");
        List<GroupDTO> groupsDTO = service.findAll();
        return new GroupResponse(groupsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Save group",
            notes = "Saving unique group to DB",
            nickname = "save",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GroupDTO.class,
            httpMethod = "POST",
            responseContainer = "GroupDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "The group created successfully",
                    response = GroupDTO.class,
                    responseContainer = "GroupDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Group not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Group is already taken",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public GroupDTO save(@ApiParam(value = "GroupCreateDTO instance") @RequestBody @Valid GroupCreateDTO groupCreateDTO,
                         BindingResult bindingResult) {
        log.info("requested- [POST]-'/api/groups/save'");
        GroupDTO saveGroupDTO = mapper.map(groupCreateDTO);
        validator.validate(saveGroupDTO, bindingResult);
        extractedErrors(bindingResult);
        GroupDTO savedGroupDTO = service.save(saveGroupDTO);
        log.info("SAVED {} SUCCESSFULLY", savedGroupDTO);
        return savedGroupDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Update group",
            notes = "Updating group from DB to new Group name must be unique and not repeat",
            nickname = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GroupDTO.class,
            httpMethod = "PATCH",
            responseContainer = "GroupDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The group updated successfully",
                    response = GroupDTO.class,
                    responseContainer = "GroupDTO"),
            @ApiResponse(
                    code = 400,
                    message = "Group not valid",
                    response = GlobalErrorResponse.class,
                    responseContainer = "GlobalErrorResponse"),
            @ApiResponse(
                    code = 404,
                    message = "Group is already taken",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public GroupDTO update(@ApiParam(value = "Group - Id") @PathVariable("id") Integer id,
                           @ApiParam(value = "GroupCreateDTO instance") @RequestBody @Valid GroupCreateDTO groupCreateDTO,
                           BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api/groups/update/{id}'");
        GroupDTO updateGroupDTO = mapper.map(groupCreateDTO);
        validator.validate(updateGroupDTO, bindingResult);
        extractedErrors(bindingResult);
        updateGroupDTO.setId(id);
        service.update(updateGroupDTO);
        log.info("UPDATING... {}", updateGroupDTO);
        return updateGroupDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete group",
            notes = "Deleting group from DB by Id",
            nickname = "deleteById",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Group deleted successfully",
                    response = ResponseEntity.class,
                    responseContainer = "ResponseEntity<?>"),
            @ApiResponse(
                    code = 404,
                    message = "Group not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public ResponseEntity<?> deleteById(@ApiParam(value = "Group - Id") @PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/groups/{id}/delete'");
        service.deleteById(id);
        log.info("DELETED GROUP BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Delete all groups",
            notes = "Deleting all groups from DB",
            nickname = "deleteAll",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseEntity.class,
            httpMethod = "DELETE",
            responseContainer = "ResponseEntity<?>")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Deleted all groups successfully",
            response = ResponseEntity.class,
            responseContainer = "ResponseEntity<?>")})
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/groups/delete/all'");
        service.deleteAll();
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_name")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find group",
            notes = "Finding group by name from DB",
            nickname = "findByName",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GroupDTO.class,
            responseContainer = "GroupDTO")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Found group by name successfully",
                    response = GroupDTO.class,
                    responseContainer = "GroupDTO"),
            @ApiResponse(
                    code = 404,
                    message = "Can't find group by name",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public GroupDTO findByName(@ApiParam(value = "Group name", example = "G-10") @RequestHeader("name") String groupName) {
        log.info("requested-> [GET]-'/api/groups/find/by_name'");
        GroupDTO result = service.findByName(groupName);
        log.info("FOUND {} GROUP BY NAME {} SUCCESSFULLY", result, groupName);
        return result;
    }

    @GetMapping("/{id}/find/students")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Find students",
            notes = "Finding students by group Id",
            nickname = "findStudents",
            produces = MediaType.APPLICATION_JSON_VALUE,
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
                    message = "Group by id not exists",
                    response = ErrorResponse.class,
                    responseContainer = "ErrorResponse")})
    public StudentsResponse findStudents(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/groups/{id}/find/students'");
        List<StudentDTO> studentsDTO = service.findStudents(id);
        return new StudentsResponse(studentsDTO);
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }
}
