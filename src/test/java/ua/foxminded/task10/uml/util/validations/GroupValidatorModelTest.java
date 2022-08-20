package ua.foxminded.task10.uml.util.validations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.Errors;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.repository.GroupRepository;
import ua.foxminded.task10.uml.util.configuration.GroupValidatorConfigurationTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GroupValidatorConfigurationTest.class, loader = AnnotationConfigContextLoader.class)
class GroupValidatorModelTest {

    @Autowired
    private GroupValidator validator;
    @Autowired
    private GroupRepository repository;
    private static final String groupName = "G-10";
    private static final GroupDTO groupDTO = mock(GroupDTO.class);
    private static final Group group = mock(Group.class);

    @BeforeAll
    static void setUp() {
        when(groupDTO.getName()).thenReturn(groupName);
    }

    @Test
    void shouldAcceptGroupWithNewName(){
        when(repository.findByName(groupName)).thenReturn(Optional.empty());

        Errors errors = mock(Errors.class);

        validator.validate(groupDTO, errors);

        verify(errors, never()).rejectValue(eq("name"), any(), any());
    }

    @Test
    void shouldRejectClassroomWithNameIsAlreadyTaken(){
        when(repository.findByName(groupName)).thenReturn(Optional.of(group));

        Errors errors = mock(Errors.class);

        validator.validate(groupDTO, errors);

        verify(errors, times(1)).rejectValue(eq("name"), any(), any());
    }
}