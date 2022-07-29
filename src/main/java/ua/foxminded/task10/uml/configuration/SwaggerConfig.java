package ua.foxminded.task10.uml.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.foxminded.task10.uml.controller.rest"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDateTime.class, String.class)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, getCustomizedResponseMessages())
                .globalResponseMessage(RequestMethod.POST, getCustomizedResponseMessages())
                .globalResponseMessage(RequestMethod.PATCH, getCustomizedResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, getCustomizedResponseMessages());
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder())
                .title("University API")
                .description("Project Spring Boot PI Swagger")
                .termsOfServiceUrl("http://localhost:8080/")
                .contact(new Contact(
                        "Oleksandr Kuzminchuk",
                        "-",
                        "lgoptimusg252@gmail.com"))
                .version("1.0")
                .build();
    }

    private List<ResponseMessage> getCustomizedResponseMessages(){
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(401).message("You are not authorized to view the resource").build());
        responseMessages.add(new ResponseMessageBuilder().code(403).message("Accessing the resource you were trying to reach is forbidden").build());
        responseMessages.add(new ResponseMessageBuilder().code(404).message("The resource you were trying to reach is not found").build());
        return responseMessages;
    }
}
