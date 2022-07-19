package ua.foxminded.task10.uml.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.mapper.SubjectMapper;
import ua.foxminded.task10.uml.dto.mapper.TeacherMapper;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherResponse;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.validations.SubjectValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectRestController {

    private final SubjectService subjectService;
    private final SubjectValidator subjectValidator;

    @GetMapping
    public SubjectResponse findAll() {
        log.info("requested-> [GET]-'/api/subjects'");
        return new SubjectResponse(subjectService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<SubjectDTO> save(@RequestBody @Valid SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api//subjects/save'");
        subjectValidator.validate(subjectDTO, bindingResult);
        extractedErrors(bindingResult);
        SubjectDTO savedSubjectDTO = subjectService.save(subjectDTO);
        log.info("SAVED {} SUCCESSFULLY", savedSubjectDTO);
        return new ResponseEntity<>(savedSubjectDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<SubjectDTO> update(@RequestBody @Valid SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api//subjects/{id}/updated'");
        subjectValidator.validate(subjectDTO, bindingResult);
        extractedErrors(bindingResult);
        SubjectDTO updatedSubjectDTO = subjectService.update(subjectDTO);
        log.info("UPDATED {} SUCCESSFULLY", updatedSubjectDTO);
        return new ResponseEntity<>(updatedSubjectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<SubjectDTO> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api//subjects/{id}/deleted'");
        SubjectDTO subjectDTO = subjectService.findById(id);
        subjectService.deleteById(id);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok(subjectDTO);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> deleteAll() {
        log.info("requested-> [DELETE]-'/api/subjects/delete/all'");
        Long countSubjects = subjectService.count();
        subjectService.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return new ResponseEntity<>(countSubjects, HttpStatus.OK);
    }

    @GetMapping("/find/by_name")
    public ResponseEntity<SubjectDTO> findByName(@RequestBody @Valid SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [GET]-'/api/subjects/find/by_name'");
        SubjectDTO result = subjectService.findByName(subjectDTO.getName());
        log.info("FOUND {} SUBJECT BY NAME {}", result, subjectDTO.getName());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{subjectId}/add/{teacherId}/teacher")
    public ResponseEntity<HttpStatus> addTeacher(@PathVariable("subjectId") Integer subjectId,
                                                 @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [POST]-'/api/subjects/{id}/add/teacher'");
        subjectService.addTeacher(subjectId, teacherId);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}/find/teachers")
    public TeacherResponse findTeachers(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/subjects/{id}/find/teachers'");
        return new TeacherResponse(subjectService.findTeachers(id));
    }

    @PatchMapping("/{subjectId}/update/{oldTeacherId}/teacher")
    public ResponseEntity<HttpStatus> updateTeacher(@RequestBody @Valid TeacherDTO newTeacherDTO, BindingResult bindingResult,
                                                    @PathVariable("oldTeacherId") Integer oldTeacherId,
                                                    @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/api/subjects/{subjectId}/update/{oldTeacherId}/teacher'");
        subjectValidator.validateUniqueTeacher(subjectId, newTeacherDTO.getId(), bindingResult);
        if (bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
        subjectService.updateTeacher(subjectId, oldTeacherId, newTeacherDTO.getId());
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacherDTO.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{subjectId}/delete/{teacherId}/teacher")
    public ResponseEntity<HttpStatus> deleteTeacher(@PathVariable("subjectId") Integer subjectId,
                                                    @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/api/subjects/{subjectId}/delete/{teacherId}/teacher'");
        subjectService.deleteTeacher(subjectId, teacherId);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }

}
