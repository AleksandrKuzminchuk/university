package ua.foxminded.task10.uml.util;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.repository.GroupRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupValidator implements Validator {

    private final GroupRepository groupRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Group.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING GROUP BY NAME {}", target);
        Group group = (Group) target;
        if (groupRepository.findByName(group.getName()).isPresent()){
            errors.rejectValue("name", "", "This name is already taken");
        }
    }
}
