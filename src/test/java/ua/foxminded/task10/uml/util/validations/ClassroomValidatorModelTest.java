package ua.foxminded.task10.uml.util.validations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.Errors;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.repository.ClassroomRepository;
import ua.foxminded.task10.uml.util.configuration.ClassroomValidatorConfigurationTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClassroomValidatorConfigurationTest.class, loader = AnnotationConfigContextLoader.class)
class ClassroomValidatorModelTest {

    @Autowired
    private ClassroomValidator validator;
    @Autowired
    private ClassroomRepository repository;
    private static final Integer classroomNumber = 45;
    private static final ClassroomDTO classroomDTO = mock(ClassroomDTO.class);
    private static final Classroom classroom = mock(Classroom.class);

    @BeforeAll
    static void setUp() {
        when(classroomDTO.getNumber()).thenReturn(classroomNumber);
    }

    @Test
    void shouldAcceptClassroomWithNewNumber(){
        when(repository.findByNumber(classroomNumber)).thenReturn(Optional.empty());

        Errors errors = mock(Errors.class);

        validator.validate(classroomDTO, errors);

        verify(errors, never()).rejectValue(eq("number"), any(), any());
    }

    @Test
    void shouldRejectClassroomWithNumberIsAlreadyTaken(){
        when(repository.findByNumber(classroomNumber)).thenReturn(Optional.of(classroom));

        Errors errors = mock(Errors.class);

        validator.validate(classroomDTO, errors);

        verify(errors, times(1)).rejectValue(eq("number"), any(), any());
    }
}