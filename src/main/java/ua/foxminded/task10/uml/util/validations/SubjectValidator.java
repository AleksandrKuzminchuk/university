package ua.foxminded.task10.uml.util.validations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.service.SubjectService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectValidator implements Validator {

    private final SubjectRepository  subjectRepository;
    private final SubjectService subjectService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubjectDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING SUBJECT BY NAME {}", target);
        SubjectDTO subjectDTO = (SubjectDTO) target;
        if (subjectRepository.findByName(subjectDTO.getName()).isPresent()){
            errors.rejectValue("name", "", "Subject [" + subjectDTO.getName() + "] is already taken");
        }
    }

    public void validateUniqueTeacher(Integer subjectId, Integer teacherId, Errors errors) {
        log.info("Validation of subjects for the uniqueness of the teacher");
        subjectService.findTeachers(subjectId).forEach(teacher ->
                {
                    if (teacher.getId().equals(teacherId)) {
                        errors.rejectValue("id", "", "The subject already has the teacher of id [" + teacherId + "]");
                    }
                }
        );
    }
}
