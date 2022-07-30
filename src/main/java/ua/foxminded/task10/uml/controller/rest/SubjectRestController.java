package ua.foxminded.task10.uml.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherResponse;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.validations.SubjectValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectRestController {

    private final SubjectService subjectService;
    private final SubjectValidator subjectValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public SubjectResponse findAll() {
        log.info("requested-> [GET]-'/api/subjects'");
        List<SubjectDTO> subjectsDTO = subjectService.findAll();
        return new SubjectResponse(subjectsDTO);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectDTO save(@RequestBody @Valid SubjectDTO subjectDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api//subjects/save'");
        subjectValidator.validate(subjectDTO, bindingResult);
        extractedErrors(bindingResult);
        SubjectDTO savedSubjectDTO = subjectService.save(subjectDTO);
        log.info("SAVED {} SUCCESSFULLY", savedSubjectDTO);
        return savedSubjectDTO;
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubjectDTO update(@RequestBody @Valid SubjectDTO subjectDTO, BindingResult bindingResult,
                                             @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api//subjects/update/{id}'");
        subjectValidator.validate(subjectDTO, bindingResult);
        extractedErrors(bindingResult);
        subjectDTO.setId(id);
        subjectService.update(subjectDTO);
        log.info("UPDATED {} SUCCESSFULLY", subjectDTO);
        return subjectDTO;
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api//subjects/{id}/deleted'");
        subjectService.deleteById(id);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/subjects/delete/all'");
        subjectService.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_name")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<SubjectDTO> findByName(@RequestBody @Valid SubjectDTO subjectDTO) {
        log.info("requested-> [GET]-'/api/subjects/find/by_name'");
        SubjectDTO result = subjectService.findByName(subjectDTO.getName());
        log.info("FOUND {} SUBJECT BY NAME {}", result, subjectDTO.getName());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{subjectId}/add/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public TeacherDTO addTeacher(@PathVariable("subjectId") Integer subjectId,
                                                 @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [POST]-'/api/subjects/{id}/add/teacher'");
        TeacherDTO teacherDTOToBeAdded = subjectService.addTeacher(subjectId, teacherId);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return teacherDTOToBeAdded;
    }

    @GetMapping("/{id}/find/teachers")
    @ResponseStatus(HttpStatus.FOUND)
    public TeacherResponse findTeachers(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/subjects/{id}/find/teachers'");
        List<TeacherDTO> teachersDTO = subjectService.findTeachers(id);
        return new TeacherResponse(teachersDTO);
    }

    @PatchMapping("/{subjectId}/update/{oldTeacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public TeacherDTO updateTeacher(@RequestBody @Valid TeacherDTO newTeacherDTO, BindingResult bindingResult,
                                                    @PathVariable("oldTeacherId") Integer oldTeacherId,
                                                    @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [PATCH]-'/api/subjects/{subjectId}/update/{oldTeacherId}/teacher'");
        subjectValidator.validateUniqueTeacher(subjectId, newTeacherDTO.getId(), bindingResult);
        extractedErrors(bindingResult);
        TeacherDTO teacherDTOToBeUpdated = subjectService.updateTeacher(subjectId, oldTeacherId, newTeacherDTO.getId());
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacherDTO.getId());
        return teacherDTOToBeUpdated;
    }

    @DeleteMapping("/{subjectId}/delete/{teacherId}/teacher")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTeacher(@PathVariable("subjectId") Integer subjectId,
                                                    @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [DELETE]-'/api/subjects/{subjectId}/delete/{teacherId}/teacher'");
        subjectService.deleteTeacher(subjectId, teacherId);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return ResponseEntity.ok().build();
    }

    private void extractedErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
    }

}
