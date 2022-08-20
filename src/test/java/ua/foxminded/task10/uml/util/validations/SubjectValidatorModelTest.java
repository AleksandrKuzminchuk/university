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
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.util.configuration.SubjectValidatorConfigurationTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SubjectValidatorConfigurationTest.class, loader = AnnotationConfigContextLoader.class)
class SubjectValidatorModelTest {

    @Autowired
    private SubjectValidator validator;
    @Autowired
    private SubjectRepository repository;
    private static final String subjectName = "GEOMETRY";
    private static final SubjectDTO subjectDTO = mock(SubjectDTO.class);
    private static final Subject subject = mock(Subject.class);

    @BeforeAll
    static void setUp() {
        when(subjectDTO.getName()).thenReturn(subjectName);
    }

    @Test
    void shouldAcceptSubjectWithNewName(){
        when(repository.findByName(subjectName)).thenReturn(Optional.empty());

        Errors errors = mock(Errors.class);

        validator.validate(subjectDTO, errors);

        verify(errors, never()).rejectValue(eq("name"), any(),any());
    }

    @Test
    void shouldRejectSubjectWithNameIsAlreadyTaken(){
        when(repository.findByName(subjectName)).thenReturn(Optional.of(subject));

        Errors errors = mock(Errors.class);

        validator.validate(subjectDTO, errors);

        verify(errors, times(1)).rejectValue(eq("name"), any(),any());
    }
}