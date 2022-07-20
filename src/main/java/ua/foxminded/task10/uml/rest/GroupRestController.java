package ua.foxminded.task10.uml.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.response.GroupResponse;
import ua.foxminded.task10.uml.dto.response.StudentsResponse;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.validations.GroupValidator;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/api/groups")
public class GroupRestController {
    private final GroupService groupService;
    private final GroupValidator groupValidator;

    @GetMapping
    public GroupResponse findAll() {
        log.info("requested- [GET]-'/api/groups");
        return new GroupResponse(groupService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<GroupDTO> save(@RequestBody @Valid GroupDTO groupDTO, BindingResult bindingResult) {
        log.info("requested- [POST]-'/api/groups/save'");
        groupValidator.validate(groupDTO, bindingResult);
        extractedErrors(bindingResult);
        GroupDTO savedGroupDTO = groupService.save(groupDTO);
        return new ResponseEntity<>(savedGroupDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<GroupDTO> update(@RequestBody @Valid GroupDTO groupDTO, BindingResult bindingResult,
                                           @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/groups/update/{id}'");
        groupValidator.validate(groupDTO, bindingResult);
        extractedErrors(bindingResult);
        groupDTO.setId(id);
        GroupDTO updatedGroupDTO = groupService.update(groupDTO);
        log.info("UPDATING... {}", updatedGroupDTO);
        return ResponseEntity.ok(updatedGroupDTO);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<GroupDTO> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/groups/{id}/delete'");
        GroupDTO groupDTO = groupService.findById(id);
        groupService.deleteById(id);
        log.info("DELETED {} SUCCESSFULLY", groupDTO);
        return ResponseEntity.ok(groupDTO);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> deleteAll() {
        log.info("requested-> [DELETE]-'/api/groups/delete/all'");
        Long countGroups = groupService.count();
        groupService.deleteAll();
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
        return new ResponseEntity<>(countGroups, HttpStatus.OK);
    }

    @GetMapping("/find/by_name")
    public ResponseEntity<GroupDTO> findByName(@RequestBody @Valid GroupDTO groupDTO, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/api/groups/find/by_name'");
        GroupDTO result = groupService.findByName(groupDTO.getName());
        log.info("FOUND {} GROUP BY NAME {} SUCCESSFULLY", result, groupDTO.getName());
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping("/{id}/find/students")
    public StudentsResponse findStudents(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/groups/{id}/find/students'");
        return new StudentsResponse(groupService.findStudents(id));
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }
}
