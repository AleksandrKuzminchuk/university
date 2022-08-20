package ua.foxminded.task10.uml.util.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.util.validations.SubjectValidator;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class SubjectValidatorConfigurationTest {

    @Bean
    public SubjectRepository subjectRepository(){
        return mock(SubjectRepository.class);
    }

    @Bean
    public SubjectValidator subjectValidator(){
        return new SubjectValidator(subjectRepository());
    }
}
