package ua.foxminded.task10.uml.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.SubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherResponse;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherRestController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;

    @GetMapping
    public TeacherResponse findAll() {
        log.info("requested-> [GET]-'/api/teachers'");
        List<TeacherDTO> teachersDTO = teacherService.findAll();
        return new TeacherResponse(teachersDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<TeacherDTO> save(@RequestBody @Valid TeacherDTO teacherDTO) {
        log.info("requested-> [POST]-'/api/teachers/save'");
        TeacherDTO savedTeacherDTO = teacherService.save(teacherDTO);
        log.info("SAVED {} SUCCESSFULLY", savedTeacherDTO);
        return new ResponseEntity<>(savedTeacherDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        log.info("requested-> [DELETE]-'/api/teachers/{id}/delete'");
        teacherService.deleteById(id);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid TeacherDTO teacherDTO,
                                             @PathVariable("id") Integer id) {
        log.info("requested-> [PATCH]-'/api/teachers/update/{id}'");
        teacherDTO.setId(id);
        teacherService.update(teacherDTO);
        log.info("UPDATED {} SUCCESSFULLY", teacherDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<?> deleteAll() {
        log.info("requested-> [DELETE]-'/api/teachers/delete/all'");
        teacherService.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/by_name_surname")
    public TeacherResponse findByNameOrSurname(@RequestBody TeacherDTO teacherDTO) {
        log.info("requested-> [GET]-'/api/teachers/find/by_name_surname'");
        List<TeacherDTO> teachersDTO = teacherService.findByNameOrSurname(teacherDTO.getFirstName(), teacherDTO.getLastName());
        return new TeacherResponse(teachersDTO);
    }

    @GetMapping("/{id}/find/subjects")
    public SubjectResponse findSubjects(@PathVariable("id") Integer id) {
        log.info("requested-> [GET]-'/api/teachers/{id}/find/subjects'");
        List<SubjectDTO> subjectsDTO = teacherService.findSubjects(id);
        return new SubjectResponse(subjectsDTO);
    }

    @PostMapping("/{id}/add/subject")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO,
                                                 @PathVariable("id") Integer id) {
        log.info("requested-> [POST]-'/api/teachers/{id}/add/subject'");
        teacherService.addSubject(id, subjectService.findByName(subjectDTO.getName()).getId());
        log.info("ADDED TEACHER BY ID - {} TO SUBJECT {} SUCCESSFULLY", id, subjectDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{teacherId}/update/{oldSubjectId}/subject")
    public ResponseEntity<HttpStatus> updateSubject(@RequestBody SubjectDTO newSubjectDTO,
                                @PathVariable("oldSubjectId") Integer oldSubjectId,
                                @PathVariable("teacherId") Integer teacherId) {
        log.info("requested-> [PATCH]-'/api/teachers/{teacherId}/update/{oldSubjectId}/subject'");
        teacherService.updateSubject(teacherId, oldSubjectId, newSubjectDTO.getId());
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY NAME - {}", teacherId, oldSubjectId, newSubjectDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{teacherId}/delete/{subjectId}/subject")
    public ResponseEntity<?> deleteSubject(@PathVariable("teacherId") Integer teacherId,
                                @PathVariable("subjectId") Integer subjectId) {
        log.info("requested-> [DELETE]-'/api/teachers/{teacherId}/delete/{subjectId}/subject'");
        teacherService.deleteSubject(teacherId, subjectId);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return ResponseEntity.ok().build();
    }
}
