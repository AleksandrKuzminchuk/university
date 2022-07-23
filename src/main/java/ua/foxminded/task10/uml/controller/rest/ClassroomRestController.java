package ua.foxminded.task10.uml.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
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
public class ClassroomRestController {

    private final ClassroomService classroomService;
    private final ClassroomValidator classroomValidator;

    @GetMapping()
    public ClassroomResponse findAll() {
        log.info("requested-> [GET]-'/api/classrooms'");
        List<ClassroomDTO> classroomsDTO = classroomService.findAll();
        return new ClassroomResponse(classroomsDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<ClassroomDTO> save(@RequestBody @Valid ClassroomDTO classroomDTO,
                                             BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api/classrooms/save'");
        classroomValidator.validate(classroomDTO, bindingResult);
        extractedErrors(bindingResult);
        ClassroomDTO savedClassroomDTO = classroomService.save(classroomDTO);
        log.info("SAVED {} SUCCESSFULLY", savedClassroomDTO);
        return new ResponseEntity<>(savedClassroomDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ClassroomDTO classroomDTO, BindingResult bindingResult,
                                               @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/classrooms/update/{id}'");
        classroomValidator.validate(classroomDTO, bindingResult);
        extractedErrors(bindingResult);
        classroomDTO.setId(id);
        classroomService.update(classroomDTO);
        log.info("UPDATED {} CLASSROOM SUCCESSFULLY", classroomDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/classrooms/{id}/delete'");
        classroomService.deleteById(id);
        log.info("DELETED CLASSROOM BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_number")
    public ResponseEntity<ClassroomDTO> findByNumber(@RequestBody ClassroomDTO classroomDTO) {
        log.info("requested-> [GET]-'/api/classrooms/find/by_number'");
        ClassroomDTO result = classroomService.findByNumber(classroomDTO.getNumber());
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", result, classroomDTO.getNumber());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/all")
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
