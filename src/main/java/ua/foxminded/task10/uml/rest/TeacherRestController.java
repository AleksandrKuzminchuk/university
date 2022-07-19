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
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;
import ua.foxminded.task10.uml.util.errors.ErrorsUtil;
import ua.foxminded.task10.uml.util.validations.TeacherValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherRestController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final TeacherValidator teacherValidator;

    @GetMapping
    public TeacherResponse findAll() {
        log.info("requested-> [GET]-'/api/teachers'");
        return new TeacherResponse(teacherService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<TeacherDTO> save(@RequestBody @Valid TeacherDTO teacherDTO, BindingResult bindingResult) {
        log.info("requested-> [POST]-'/api/teachers/save'");
        TeacherDTO savedTeacherDTO = teacherService.save(teacherDTO);
        log.info("SAVED {} SUCCESSFULLY", savedTeacherDTO);
        return new ResponseEntity<>(savedTeacherDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<TeacherDTO> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/teachers/{id}/delete'");
        TeacherDTO teacherDTO = teacherService.findById(id);
        teacherService.deleteById(id);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok(teacherDTO);
    }

    @PatchMapping("/update")
    public ResponseEntity<TeacherDTO> update(@RequestBody @Valid TeacherDTO teacherDTO, BindingResult bindingResult) {
        log.info("requested-> [PATCH]-'/api/teachers/{id}/update'");
        TeacherDTO updatedTeacherDTO = teacherService.update(teacherDTO);
        log.info("UPDATED {} SUCCESSFULLY", updatedTeacherDTO);
        return ResponseEntity.ok(updatedTeacherDTO);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> deleteAll() {
        log.info("requested-> [DELETE]-'/api/teachers/delete/all'");
        Long countTeachers = teacherService.count();
        teacherService.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return new ResponseEntity<>(countTeachers, HttpStatus.OK);
    }

    @GetMapping("/find/by_name_surname")
    public TeacherResponse findByNameOrSurname(@RequestBody TeacherDTO teacherDTO) {
        log.info("requested-> [GET]-'/api/teachers/find/by_name_surname'");
        return new TeacherResponse(teacherService.findByNameOrSurname(teacherDTO.getFirstName(), teacherDTO.getLastName()));
    }

    @GetMapping("/{id}/find/subjects")
    public SubjectResponse findSubjects(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/teachers/{id}/find/subjects'");
        return new SubjectResponse(teacherService.findSubjects(id));
    }

    @PostMapping("/{id}/add/subject")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody @Valid SubjectDTO subjectDTO,
                                                 BindingResult bindingResult,
                                                 @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/api/teachers/{id}/add/subject'");
        teacherValidator.validateUniqueSubject(id, subjectDTO.getName(), bindingResult);
        if (bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
        teacherService.addSubject(id, subjectService.findByName(subjectDTO.getName()).getId());
        log.info("ADDED TEACHER BY ID - {} TO SUBJECT {} SUCCESSFULLY", id, subjectDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{teacherId}/update/{oldSubjectId}/subject")
    public ResponseEntity<HttpStatus> updateSubject(@RequestBody @Valid SubjectDTO newSubjectDTO,
                                BindingResult bindingResult,
                                @PathVariable("oldSubjectId") Integer oldSubjectId,
                                @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'/api/teachers/{teacherId}/update/{oldSubjectId}/subject'");
        teacherValidator.validateUniqueSubject(teacherId, newSubjectDTO.getName(), bindingResult);
        if (bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(ErrorsUtil::returnErrorsToClient);
        }
        teacherService.updateSubject(teacherId, oldSubjectId, newSubjectDTO.getId());
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY NAME - {}", teacherId, oldSubjectId, newSubjectDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{teacherId}/delete/{subjectId}/subject")
    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable("teacherId") Integer teacherId,
                                @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/api/teachers/{teacherId}/delete/{subjectId}/subject'");
        teacherService.deleteSubject(teacherId, subjectId);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
