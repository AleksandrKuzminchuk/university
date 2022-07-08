package ua.foxminded.task10.uml.util;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.repository.ClassroomRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClassroomValidator implements Validator {

    public final ClassroomRepository classroomRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Classroom.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING CLASSROOM BY NUMBER {}", target);
        Classroom classroom = (Classroom) target;
        if (classroomRepository.findClassroomByNumber(classroom.getNumber()).isPresent()) {
            errors.rejectValue("number", "", "This number is already taken");
        }
    }
}
