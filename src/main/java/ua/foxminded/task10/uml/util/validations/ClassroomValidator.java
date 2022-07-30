package ua.foxminded.task10.uml.util.validations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.repository.ClassroomRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClassroomValidator implements Validator {

    public final ClassroomRepository classroomRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ClassroomDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING CLASSROOM BY NUMBER {}", target);
        ClassroomDTO classroomDTO = (ClassroomDTO) target;
        if (classroomRepository.findByNumber(classroomDTO.getNumber()).isPresent()) {
            errors.rejectValue("number", "", "Classroom [" + classroomDTO.getNumber() + "] is already taken");
        }
    }
}
