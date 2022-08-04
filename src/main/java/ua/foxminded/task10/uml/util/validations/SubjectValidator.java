package ua.foxminded.task10.uml.util.validations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.repository.SubjectRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectValidator implements Validator {

    private final SubjectRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubjectDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING SUBJECT BY NAME {}", target);
        SubjectDTO subjectDTO = (SubjectDTO) target;
        if (repository.findByName(subjectDTO.getName()).isPresent()){
            errors.rejectValue("name", "", "Subject [" + subjectDTO.getName() + "] is already taken");
        }
    }
}
