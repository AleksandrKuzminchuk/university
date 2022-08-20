package ua.foxminded.task10.uml.util.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.task10.uml.dto.response.GroupResponse;
import ua.foxminded.task10.uml.repository.GroupRepository;
import ua.foxminded.task10.uml.util.validations.GroupValidator;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class GroupValidatorConfigurationTest {

    @Bean
    public GroupRepository groupRepository(){
        return mock(GroupRepository.class);
    }

    @Bean
    public GroupValidator groupValidator(){
        return new GroupValidator(groupRepository());
    }
}
