package ua.foxminded.task10.uml.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.repository.SubjectRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectValidator implements Validator {

    private final SubjectRepository  subjectRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Subject.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING SUBJECT BY NAME {}", target);
        Subject subject = (Subject) target;
        if (subjectRepository.findSubjectsByName(subject.getName()).isPresent()){
            errors.rejectValue("name", "", "This name is already taken");
        }
    }
}
