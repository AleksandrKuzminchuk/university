package ua.foxminded.task10.uml.util.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.NestedTestConfiguration;
import ua.foxminded.task10.uml.repository.ClassroomRepository;
import ua.foxminded.task10.uml.util.validations.ClassroomValidator;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ClassroomValidatorConfigurationTest {

    @Bean
    public ClassroomRepository classroomRepository(){
        return mock(ClassroomRepository.class);
    }

    @Bean
    public ClassroomValidator classroomValidator(){
        return new ClassroomValidator(classroomRepository());
    }
}
