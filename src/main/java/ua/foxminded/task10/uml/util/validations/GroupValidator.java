package ua.foxminded.task10.uml.util.validations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.repository.GroupRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupValidator implements Validator {

    private final GroupRepository groupRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return GroupDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("VALIDATING GROUP BY NAME {}", target);
        GroupDTO group = (GroupDTO) target;
        if (groupRepository.findByName(group.getName()).isPresent()){
            errors.rejectValue("name","", "Group [" + group.getName() + "] is already taken");
        }
    }
}
