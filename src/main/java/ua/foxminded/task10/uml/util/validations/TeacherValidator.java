package ua.foxminded.task10.uml.util.validations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.service.TeacherService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeacherValidator implements Validator {

    private final TeacherService teacherService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validateUniqueSubject(Integer teacherId, String subjectName, Errors errors) {
        log.info("Validation of teachers for the uniqueness of the subject");
        teacherService.findSubjects(teacherId).forEach(subject ->
                {
                    if (subject.getName().equals(subjectName)) {
                        errors.rejectValue("name", "", "The teacher already has the subject of [" + subjectName + "]");
                    }
                }
        );
    }
}
